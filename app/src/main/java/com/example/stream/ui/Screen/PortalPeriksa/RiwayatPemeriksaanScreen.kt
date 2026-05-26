package com.example.stream.ui.Screen.PortalPeriksa

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stream.Data.Local.UserPreferences
import com.example.stream.ui.Screen.Berita.formatTanggalIndonesia

@Composable
fun CardRiwayatItem(
    date: String?,
    judul: String?,
    alamat: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = date?.let { formatTanggalIndonesia(it) } ?: "-", fontWeight = FontWeight.Bold, color = Color(0xFF1A3C40))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Article,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = judul ?: "-", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = alamat ?: "-", fontSize = 14.sp)
                    }
                }

                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.align(Alignment.Top)
                ) {
                    Text("Selengkapnya", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun RiwayatPemeriksaanScreen(
    navController: NavController,
    viewModel: PortalPeriksaViewModel,
    wargaId: Int,
    nik: String,
    tipe: String
) {
    val context = LocalContext.current
    val token = remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val riwayat by viewModel.riwayat.collectAsState()

    val userId by UserPreferences.getUserId(context).collectAsState(initial = 0)

    LaunchedEffect(Unit) {
        token.value = UserPreferences.getToken(context).toString()

        token.let {
            userId?.let { it1 -> viewModel.fetchRiwayat(token, it1, nik, tipe) }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF0F0F0))
        .padding(top = 16.dp)) {

        // Header (Back + Title)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Kembali",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Riwayat Pemeriksaan",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0A1D2D)
            )
        }

        // Konten utama
        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Box(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp), contentAlignment = Alignment.Center) {
                    RiwayatKosongCard()
                }
            }

            riwayat.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tidak ada riwayat pemeriksaan.")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                ) {
                    items(riwayat) { item ->
                        CardRiwayatItem(
                            date = item.tanggal,
                            judul = item.judul_berita,
                            alamat = item.lokasi_pelaksanaan,
                            onClick = {
                                val id = item.id
                                navController.navigate("pemeriksaan/${id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RiwayatKosongCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF8F9FA))
            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Riwayat Pemeriksaan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF08607A)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Anggota belum melakukan pemeriksaan.",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewRiwayatPemeriksaanScreen() {
//    val dummyViewModel = object : PortalPeriksaViewModel(repository = object : PortalPeriksaRepository {
//        override suspend fun getRiwayat(
//            bearer: String,
//            wargaId: Int,
//            nik: String,
//            tipe: String
//        ) = runCatching {
//            com.example.stream.Data.Model.Response.PortalPeriksaResponse(
//                nama_anggota = "Budi",
//                riwayat = listOf(
//                    RiwayatItem(
//                        tanggal = "2025-05-10",
//                        nama_posyandu = "Posyandu Mawar",
//                        alamat = "Jl. Melati No. 123"
//                    )
//                )
//            )
//        }
//    }) {}
//
//    RiwayatPemeriksaanScreen(
//        viewModel = dummyViewModel,
//        bearer = "Bearer dummy",
//        wargaId = 1,
//        nik = "1234567890123456",
//        tipe = "anak"
//    )
}
