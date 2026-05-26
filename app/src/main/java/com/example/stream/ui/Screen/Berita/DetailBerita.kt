package com.example.stream.ui.Screen.Berita

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.stream.Data.Local.UserPreferences
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

@Composable
fun DetailBeritaScreen(
    navController: NavController,
    viewModel: PortalBeritaViewModel,
    id: Int
) {
    val context = LocalContext.current
    val token by UserPreferences.getToken(context).collectAsState(initial = "")
    val userId by UserPreferences.getUserId(context).collectAsState(initial = 0)

    val anggotaTerdaftarState by viewModel.uiStateTerdaftar.collectAsState()


    LaunchedEffect(Unit) {
        val bearerToken = "Bearer $token"
        viewModel.fetchBeritaDetail(bearerToken, id)
        userId?.let { viewModel.fetchAnggotaTerdaftar(bearerToken, id, it) }
    }

    val anggotaList = when (anggotaTerdaftarState) {
        is UiStateTerdaftar.Success -> (anggotaTerdaftarState as UiStateTerdaftar.Success).data.data
        else -> emptyList()
    }

    val uiState by viewModel.uiStateDetail.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp)) {

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Kembali",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Detail Berita",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0A1D2D)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (uiState) {
            is UiStateDetail.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiStateDetail.Error -> {
                Text(
                    text = "Gagal: ${(uiState as UiStateDetail.Error).message}",
                    color = Color.Red
                )
            }

            is UiStateDetail.Success -> {
                val data = (uiState as UiStateDetail.Success).data

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.LightGray, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = data.judul ?: "-",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF0A1D2D),
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFFFFD966),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = data.kategori ?: "-",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(Icons.Outlined.Place, "Tempat", data.tempat ?: "-")
                        InfoRow(Icons.Outlined.DateRange, "Tanggal", formatTanggalIndonesia(data.tanggal) ?: "-")
                        InfoRow(Icons.Outlined.DateRange, "Waktu", data.waktu ?: "-")

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = data.deskripsi ?: "-",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Mohon untuk membawa Buku KIA (Kesehatan Ibu dan Anak) saat hadir ke lokasi.",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                when (anggotaTerdaftarState) {
                    is UiStateTerdaftar.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiStateTerdaftar.Error -> {
                        Text(
                            text = "Gagal: ${(anggotaTerdaftarState  as UiStateTerdaftar.Error).message}",
                            color = Color.Red
                        )
                    }
                    is UiStateTerdaftar.Success -> {
                        Spacer(modifier = Modifier.height(8.dp))
                        anggotaList.forEach { anggota ->
                            AnggotaCard(
                                nama = anggota.nama,
                                posisi = anggota.posisi,
                                nomor_antrian = anggota.nomor_antrian
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    else -> {}
                }

                Spacer(modifier = Modifier.weight(1f))

                val waktuMulai = data.waktu?.split(" - ")?.getOrNull(0)?.trim() ?: ""

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val acaraDateTime = try {
                    LocalDateTime.parse("${data.tanggal} $waktuMulai", formatter)
                } catch (e: Exception) {
                    Log.d("DEBUG", "Gagal parse datetime: ${e.message}")
                    null
                }

                val now = LocalDateTime.now()
                val acaraBelumLewat = acaraDateTime?.let {
                    val mulai = it
                    val selesai = it.plusHours(3)
                    now.isBefore(selesai)
                } == true

                if (acaraBelumLewat) {
                    Button(
                        onClick = {
                            navController.navigate("antrian/${data.id}")
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A6C82)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = "Daftar Antrian",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
//                Log.d("DEBUG", "Acara: $acaraDateTime")
//                Log.d("DEBUG", "Now: $now")
//                Log.d("DEBUG", "Tombol muncul: $acaraBelumLewat")
//
//                Log.d("DEBUG", "Tanggal: ${data.tanggal}")
//                Log.d("DEBUG", "Waktu: ${data.waktu}")
            }

            else -> {}
        }
    }
}


@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label : $value",
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
private fun AnggotaCard(
    nama: String,
    posisi: String,
    nomor_antrian: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(nama, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF006064))
            Spacer(modifier = Modifier.height(4.dp))
            Text("Posisi: $posisi", fontSize = 14.sp)
            Text("Antrian: $nomor_antrian", fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailBerita() {
//    DetailBeritaScreen()
}
