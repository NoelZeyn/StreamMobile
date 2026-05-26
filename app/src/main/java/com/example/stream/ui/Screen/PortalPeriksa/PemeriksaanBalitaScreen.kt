package com.example.stream.ui.Screen.PortalPeriksa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stream.Data.Local.UserPreferences
import com.example.stream.ui.Screen.components.SectionTitle
import com.example.stream.ui.Screen.components.GridData
import com.example.stream.ui.Screen.components.InfoCard

@Composable
fun PemeriksaanBalitaScreen(
    viewModel: PortalPeriksaViewModel,
    pemeriksaanId: Int
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(16.dp)
    ) {
        val uiState by viewModel.uiState.collectAsState()
        val context = LocalContext.current
        val token by UserPreferences.getToken(context).collectAsState(initial = "")

        LaunchedEffect(token) {
            if (!token.isNullOrEmpty()) {
                val bearerToken = "Bearer $token"
                viewModel.fetchPemeriksaan(bearerToken, pemeriksaanId)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F6F6))
                .padding(16.dp)
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is UiState.Error -> {
                    val error = (uiState as UiState.Error).message
                    Text("Error: $error", color = Color.Red)
                }

                is UiState.Success -> {
                    val data = (uiState as UiState.Success).data

                    // Header
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(data.nama_pasien ?: "-", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("🏡 ${data.posyandu ?: "-"}")
                            Text("📍 ${data.alamat_posyandu ?: "-"}")
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    SectionTitle("Informasi Pemeriksaan")
                    GridData(
                        data = listOf(
                            "Berat Badan" to "${data.berat_badan ?: "-"} Kg",
                            "Tinggi Badan" to "${data.tinggi_badan ?: "-"} cm",
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SectionTitle("Asupan Gizi")
                    GridData(
                        data = listOf(
                            "PMT" to (data.PMT ?: "-"),
                            "Jumlah Pemberian" to "${data.total_PMT} Kg"
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    GridData(
                        data = listOf(
                            "Asi" to if (data.ASI == true) "Ya" else "Tidak",
                            "Vit A" to (data.vit ?: "-")
                        ),
                        columns = 3
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    SectionTitle("Imunisasi dan Suplemen")

                    InfoCard(
                        title = "Imunisasi",
                        value = data.imunisasi ?: "-",
                        bgColor = Color(0xFFEDE7F6),
                        icon = "✏️"
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun GridData(data: List<Pair<String, String>>, columns: Int = 2) {
    Column {
        data.chunked(columns).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { (label, value) ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(label, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(value, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                // Jika kolom ganjil, beri spacer agar rata
                if (row.size < columns) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun InfoCard(title: String, value: String, bgColor: Color, icon: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(title, fontSize = 12.sp)
                Text(value, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPemeriksaanBalitaScreen() {
//    PemeriksaanBalitaScreen()
}
