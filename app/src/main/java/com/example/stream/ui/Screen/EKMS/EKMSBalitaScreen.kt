package com.example.stream.ui.Screen.EKMS

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stream.Data.Local.UserPreferences

@Composable
fun EKMSBalitaScreen(
    navController: NavController,
    viewModel: EKMSViewModel,
    nik: String
) {
    val context = LocalContext.current
    val token by UserPreferences.getToken(context).collectAsState(initial = "")

    val loadUiState by viewModel.loadUiState.collectAsState()
    val getPerkembanganUiState by viewModel.perkembanganData.collectAsState()

    LaunchedEffect(nik) {
        token?.let {
            viewModel.getPerkembangan(it, nik)
            viewModel.loadData(it, nik)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Kembali",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "E-KMS Balita",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0A1D2D)
            )
        }

        // Informasi Balita
        when(loadUiState) {
            is LoadUiState.Loading -> Text(text = "Sedang mengambil data")
            is LoadUiState.Error -> {
                EKMSKosongCard()
            }
            is LoadUiState.Success -> {
                val data = (loadUiState as LoadUiState.Success).data

                if (data == null) {
                    Text("Belum ada pemeriksaan untuk anggota yang dipilih.", color = Color.Gray)
                } else {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F2F7)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(data.nama, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoRowWithIconBalita(Icons.Default.Cake, "Usia Balita", data.umur)
                            InfoRowWithIconBalita(Icons.Default.Favorite, "Berat Badan", data.berat_badan.toString())
                            InfoRowWithIconBalita(Icons.Default.Star, "Kategori", data.kategori, isBadge = true)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabel perkembangan balita
//            TabelPemeriksaanBalita(tableData = tableData)
        when(getPerkembanganUiState) {
            is PerkembanganUiState.Loading -> {
                Text("Loading data perkembangan...")
            }

            is PerkembanganUiState.Error -> {
                val error = (getPerkembanganUiState as PerkembanganUiState.Error).message
//                Text("Error: $error", color = Color.Red)
            }

            is PerkembanganUiState.Success -> {
                val data = (getPerkembanganUiState as PerkembanganUiState.Success).data
                if (data.isEmpty()) {
                    Text("Belum ada pemeriksaan untuk anggota yang dipilih.", color = Color.Gray)
                } else {
                    val tableData = data.map {
                        listOf(
                            it.tanggal ?: "-",
                            it.umur,
                            it.tinggi_badan?.toString() ?: "-",
                            it.berat_badan?.toString() ?: "-"
                        )
                    }

                    TabelPemeriksaanBalita(tableData = tableData, navController, nik)

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Imunisasi dan Suplemen", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    SuplemenCardBalita("Imunisasi", Color(0xFFE8E5FF), onClick = {navController.navigate("riwayat-ekms/$nik/3")})
                    Spacer(modifier = Modifier.height(8.dp))
                    SuplemenCardBalita("Suplemen Tambahan", Color(0xFFFFF2CF), onClick = {navController.navigate("riwayat-ekms/$nik/4")})
                    Spacer(modifier = Modifier.height(8.dp))
                    SuplemenCardBalita("Vitamin", Color(0xFFFFE0E5), onClick = {navController.navigate("riwayat-ekms/$nik/2")})
                }
            }
        }
    }
}

@Composable
fun TabelPemeriksaanBalita(
    tableData: List<List<String>>,
    navController: NavController,
    nik: String
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Tabel Perkembangan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            val option = 0
            Button(
                onClick = { navController.navigate("riwayat-ekms/$nik/$option") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(32.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Selengkapnya",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(15.dp))
                .border(1.dp, Color(0xFFE5E5E5), RoundedCornerShape(15.dp))
                .clip(RoundedCornerShape(15.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFCCE5FF))
                    .height(44.dp)
            ) {
                listOf("Tanggal", "Umur", "BB", "TB").forEach {
                    TableCellBalita(text = it, isHeader = true, modifier = Modifier.weight(1f))
                }
            }

            tableData.forEachIndexed { index, row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (index % 2 == 0) Color(0xFFF9F9F9) else Color.White)
                        .height(40.dp)
                ) {
                    row.forEach {
                        TableCellBalita(text = it, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRowWithIconBalita(
    icon: ImageVector,
    label: String,
    value: String,
    isBadge: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(18.dp)
                .padding(end = 8.dp),
            tint = Color.DarkGray
        )
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(150.dp)
        )
        Text(text = ":", modifier = Modifier.padding(horizontal = 4.dp))
        if (isBadge) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFFFD966), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(value, fontSize = 12.sp, color = Color.White)
            }
        } else {
            Text(value)
        }
    }
}

@Composable
fun EKMSKosongCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF8F9FA))
            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Column {
            Text(
                text = "EKMS Belum Tersedia",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF08607A)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Belum ada pemeriksaan untuk anggota terkait.",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun TableCellBalita(
    text: String,
    isHeader: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isHeader) FontWeight.SemiBold else FontWeight.Normal,
            maxLines = 1
        )
    }
}

fun getSuplemenIconColorBalita(label: String): Color {
    return when (label) {
        "Imunisasi" -> Color(0xFF7C4DFF)
        "Suplemen Tambahan" -> Color(0xFFF7B801)
        "Vitamin" -> Color(0xFFFF4081)
        else -> Color.DarkGray
    }
}

@Composable
fun SuplemenCardBalita(
    label: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = getSuplemenIconColorBalita(label)
                )
            }
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEKMSBalitaScreen() {
//        val sampleData = listOf(
//            listOf("01-01-2025", "12 Bulan", "10 Kg", "75 cm", "46 cm"),
//            listOf("01-02-2025", "13 Bulan", "10.5 Kg", "76 cm", "47 cm"),
//            listOf("01-03-2025", "14 Bulan", "11 Kg", "77 cm", "47.5 cm"),
//            listOf("01-04-2025", "15 Bulan", "11.2 Kg", "78 cm", "48 cm"),
//            listOf("01-05-2025", "16 Bulan", "11.5 Kg", "79 cm", "48.5 cm"),
//            listOf("01-06-2025", "17 Bulan", "11.8 Kg", "80 cm", "49 cm")
//        )
//        EKMSBalitaScreen(tableData = sampleData)
}
