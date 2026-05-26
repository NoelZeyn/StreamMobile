package com.example.stream.ui.Screen.EKMS

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stream.Data.Local.UserPreferences
import com.example.stream.R
import com.example.stream.ui.Screen.PortalPeriksa.PortalPeriksaViewModel

@Composable
fun EKMSScreen(
    navController: NavController,
    viewModel: PortalPeriksaViewModel
) {
    val anggota by viewModel.anggota.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    val context = LocalContext.current

    val token by UserPreferences.getToken(context).collectAsState(initial = "")
    val no_kk by UserPreferences.getNoKK(context).collectAsState(initial = "")
    val userId by UserPreferences.getUserId(context).collectAsState(initial = 0)

    LaunchedEffect(token, no_kk) {
        if (!token.isNullOrEmpty() && !no_kk.isNullOrEmpty()) {
            val bearerToken = "Bearer $token"
            viewModel.fetchAnggota(bearerToken, no_kk!!)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // AppBar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("E-KMS", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Terjadi kesalahan: $error")
            }
        } else {
            LazyColumn{
                items(anggota) { item ->
                    val gradient = when (item.posisi_keluarga.lowercase()) {
                        "anak" -> Brush.horizontalGradient(
                            listOf(Color(0xFFFFD54F), Color(0xFFFFF176))
                        )
                        "istri", "kepala keluarga" -> Brush.horizontalGradient(
                            listOf(Color(0xFFF76D95), Color(0xFFFF8DA1))
                        )
                        else -> Brush.linearGradient(
                            colors = listOf(Color.Gray, Color.DarkGray)
                        )
                    }

                    val iconRes = when (item.posisi_keluarga.lowercase()) {
                        "anak" -> R.drawable.iconanak
                        "istri", "kepala keluarga" -> R.drawable.iconibu
                        else -> R.drawable.iconibu
                    }
                    
                    CardEKMS(
                        title = item.posisi_keluarga,
                        name = item.nama_anggota_keluarga,
                        gradient = gradient,
                        imageRes = iconRes,
                        onClick = {
                            val posisi = item.posisi_keluarga.lowercase()
                            if (posisi == "anak") {
                                val nik = item.anggota_keluarga_nik
                                if (!nik.isNullOrEmpty()) {
                                    navController.navigate("ekms/$nik")
                                } else {
                                    Toast.makeText(context, "NIK tidak tersedia untuk ${item.nama_anggota_keluarga}", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Saat ini aplikasi hanya mendukung data anak.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                // Kartu Ibu
//                CardEKMS(
//                    title = "Ibu",
//                    name = "Bunga Citra",
//                    gradient = Brush.horizontalGradient(
//                        listOf(Color(0xFFF76D95), Color(0xFFFF8DA1))
//                    ),
//                    imageRes = R.drawable.iconibu // Pastikan file ini ada di drawable
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))

                // Kartu Anak
//                CardEKMS(
//                    title = "Anak",
//                    name = "Atharafie Hermawan",
//                    gradient = Brush.horizontalGradient(
//                        listOf(Color(0xFFFFD54F), Color(0xFFFFF176))
//                    ),
//                    imageRes = R.drawable.iconanak // Pastikan file ini ada di drawable
//                )
            }
        }
    }
}

@Composable
fun CardEKMS(
    title: String,
    name: String,
    gradient: Brush,
    imageRes: Int,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEKMSScreen() {
//    EKMSScreen()
}
