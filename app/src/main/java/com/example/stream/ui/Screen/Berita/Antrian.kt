package com.example.stream.ui.Screen.Berita

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stream.Data.Local.UserPreferences
import com.example.stream.Data.Model.Response.BeritaDetailItem

@Composable
fun KonfirmasiPendaftaranScreen(
    navController: NavController,
    viewModel: PortalBeritaViewModel,
    id: Int
) {
    val context = LocalContext.current
    val token by UserPreferences.getToken(context).collectAsState(initial = "")
    val wargaNik by UserPreferences.getNik(context).collectAsState(initial = "")
    val no_kk by UserPreferences.getNoKK(context).collectAsState(initial = "")

    val beritaDetailState by viewModel.uiStateDetail.collectAsState()
    val beritaAntrianState by viewModel.uiStateAntrian.collectAsState()
    val daftarAntrianState by viewModel.uiStateDaftar.collectAsState()

    val toastMessage by viewModel.toastMessage

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.resetToastMessage()
        }
    }

    LaunchedEffect(daftarAntrianState) {
        if (daftarAntrianState is DaftarAntrianUiState.Success) {
            val nomorAntrian = (daftarAntrianState as DaftarAntrianUiState.Success).data.nomor_antrian
            navController.navigate("detail_antrian/$nomorAntrian")
            viewModel.resetUiStateDaftar()
        }
    }

    val anggotaState by viewModel.uiStateAnggota.collectAsState()

    val anggotaList = when (anggotaState) {
        is UiStateAnggota.Success -> (anggotaState as UiStateAnggota.Success).data
        else -> emptyList()
    }

    var selectedNik by remember { mutableStateOf("") }

    LaunchedEffect(token, id, no_kk) {
        if (token?.isNotEmpty() == true) {
            val bearer = "Bearer $token"
            viewModel.fetchBeritaDetail(bearer, id)
            viewModel.fetchBeritaAntrian(bearer, id)
            no_kk?.let { viewModel.fetchAnggotaBerita(bearer, it) }
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Kembali",
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Konfirmasi Pendaftaran", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Nomor Antrian Card, ambil dari beritaAntrianState
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Nomor Antrian",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                val nomorAntrian = when (beritaAntrianState) {
                    is UiStateAntrian.Success -> (beritaAntrianState as UiStateAntrian.Success).data.nomor
                    else -> 0
                }
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF008CBA), Color(0xFF005F73)),
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (nomorAntrian > 0) nomorAntrian.toString() else "-",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Yakin melanjutkan pendaftaran?",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card Detail Kegiatan dari beritaDetailState
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val detail = when (beritaDetailState) {
                    is UiStateDetail.Success -> (beritaDetailState as UiStateDetail.Success).data
                    else -> null
                }

                Text(
                    detail?.judul ?: "Judul kegiatan tidak tersedia",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF006064),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                DetailRow(Icons.Default.Place, "Tempat", detail?.tempat ?: "-")
                DetailRow(Icons.Default.DateRange, "Tanggal", detail?.tanggal ?: "-")
                DetailRow(Icons.Default.DateRange, "Waktu", detail?.waktu ?: "-")

                Spacer(modifier = Modifier.height(12.dp))
                Text(detail?.deskripsi ?: "Deskripsi tidak tersedia", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(15.dp))
                Text("Mohon untuk membawa Buku KIA (Kesehatan Ibu dan Anak) saat hadir ke lokasi.", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (anggotaState) {
            is UiStateAnggota.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is UiStateAnggota.Error -> {
                Text(
                    text = (anggotaState as UiStateAnggota.Error).message,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is UiStateAnggota.Success -> {
                Text("Pilih Anggota yang Didaftarkan", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                anggotaList.forEach { anggota ->
                    PilihAnggotaCard(
                        nama = anggota.nama_anggota_keluarga,
                        posisi = anggota.posisi_keluarga,
                        nik = anggota.nik,
                        selected = anggota.nik == selectedNik,
                        onClick = { selectedNik = anggota.nik }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (!token.isNullOrEmpty() && !wargaNik.isNullOrEmpty()) {
                    val bearer = "Bearer $token"
                    wargaNik?.let { nikWarga ->
                        viewModel.daftarAntrian(
                            bearer = bearer,
                            wargaNik = nikWarga,
                            nik = selectedNik,
                            beritaId = id,
                            tipePemeriksaan = "balita"
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            enabled = daftarAntrianState !is DaftarAntrianUiState.Loading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00627C))
        ) {
            if (daftarAntrianState is DaftarAntrianUiState.Loading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
            } else {
                Text("Daftar Antrian", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            border = BorderStroke(1.dp, Color(0xFF00627C))
        ) {
            Text("Batal", fontWeight = FontWeight.Medium, color = Color(0xFF00627C))
        }

        if (daftarAntrianState is DaftarAntrianUiState.Error) {
            Text(
                text = (daftarAntrianState as DaftarAntrianUiState.Error).message,
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}


//@Composable
//fun PilihAnggotaCard(
//    nama: String,
//    posisi: String,
//    nik: String,
//    selected: Boolean,
//    onClick: () -> Unit
//) {
//    val backgroundColor = if (selected) Color(0xFFE0F7FA) else Color.White
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClick() },
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = backgroundColor),
//        elevation = CardDefaults.cardElevation(2.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(nama, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF006064))
//            Spacer(modifier = Modifier.height(4.dp))
//            Text("Posisi: $posisi", fontSize = 14.sp)
//            Text("NIK: $nik", fontSize = 14.sp)
//        }
//    }
//}

@Composable
fun PilihAnggotaCard(
    nama: String,
    posisi: String,
    nik: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) Color(0xFFE0F7FA) else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = nama,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF006064)
            )
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(label = "Posisi", value = posisi)
            InfoRow(label = "NIK", value = nik)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = "$label",
            fontSize = 14.sp,
            modifier = Modifier.width(60.dp) // pastikan cukup untuk label terpanjang
        )
        Text(
            text = ": $value",
            fontSize = 14.sp
        )
    }
}



@Composable
fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("$label : $value", fontSize = 14.sp)
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Preview(showBackground = true)
@Composable
fun KonfirmasiPendaftaranPreview() {
//    KonfirmasiPendaftaranScreen()
}
