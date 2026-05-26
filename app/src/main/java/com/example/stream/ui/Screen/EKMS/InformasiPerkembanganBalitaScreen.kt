package com.example.stream.ui.Screen.EKMS

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stream.Data.Local.UserPreferences

@Composable
fun InformasiPerkembanganBalitaScreen(
    navController: NavController,
    viewModel: EKMSViewModel,
    nik: String,
    option: Int
) {
    val context = LocalContext.current
    val token by UserPreferences.getToken(context).collectAsState(initial = "")

    val uiState by viewModel.pemeriksaanList.collectAsState()

    val options = listOf("Informasi Berat Badan", "Informasi Tinggi Badan", "Informasi Vitamin", "Informasi Imunisasi", "Informasi Suplemen")
    var selectedOption by remember { mutableStateOf(options[option]) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(selectedOption) {
        val definisi = when(selectedOption) {
            "Informasi Tinggi Badan" -> "TinggiBadan"
            "Informasi Berat Badan" -> "BeratBadan"
            "Informasi Vitamin" -> "Vit"
            "Informasi Imunisasi" -> "Imun"
            "Informasi Suplemen" -> "Suplement"
            else -> ""
        }
        token?.let { viewModel.getEkms(it, nik, definisi) }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
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
                    text = "Informasi Perkembangan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0A1D2D)
                )
            }

            // Dropdown
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
            ) {
                OutlinedTextField(
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Pilih Informasi") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "dropdown")
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = false
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { label ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                selectedOption = label
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = selectedOption,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007BFF)
            )

            Spacer(modifier = Modifier.height(8.dp))

            when(uiState) {
                is PemeriksaanUiState.Loading -> Text(text = "Memuat data....")
                is PemeriksaanUiState.Error -> Text(text = "Terjadi kesalahan: ${(uiState as PemeriksaanUiState.Error).message}")
                is PemeriksaanUiState.Success -> {
                    val data = (uiState as PemeriksaanUiState.Success).data
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.LightGray),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .background(Color(0xFFF9F9F9))
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TableCellPerkembanganBalita("Umur", true, Modifier.weight(1f))
                                TableCellPerkembanganBalita(
                                    if (selectedOption == "Informasi Tinggi Badan") {
                                        "Tinggi Badan"
                                    } else if (selectedOption == "Informasi Berat Badan") {
                                        "Berat Badan"
                                    } else if (selectedOption == "Informasi Vitamin") {
                                        "Vitamin"
                                    } else if (selectedOption == "Informasi Suplement") {
                                        "Suplement"
                                    } else {
                                        "Imunisasi"
                                    },
                                    true,
                                    Modifier.weight(1f)
                                )
                                TableCellPerkembanganBalita(
                                    if (selectedOption == "Informasi Tinggi Badan" || selectedOption == "Informasi Berat Badan") {
                                        "Status Gizi"
                                    } else {
                                        "Tanggal Pemberian"
                                    },
                                    true, Modifier.weight(1f))
                            }
                            Divider()

                            data.forEach { item ->
                                val umur = item.umur
                                val nilai = if (selectedOption == "Informasi Tinggi Badan") {
                                    item.tinggi_badan?.toString() ?: "-"
                                } else if (selectedOption == "Informasi Berat Badan") {
                                    item.berat_badan?.toString() ?: "-"
                                } else if (selectedOption == "Informasi Vitamin") {
                                    item.vitamin ?: "-"
                                } else if (selectedOption == "Informasi Suplement") {
                                    item.suplemen ?: "-"
                                } else {
                                    item.imunisasi ?: "-"
                                }
                                val nilai2 = if (selectedOption == "Informasi Tinggi Badan" || selectedOption == "Informasi Berat Badan") {
                                    item.status_gizi ?: "-"
                                } else {
                                    item.tanggal ?: "-"
                                }
                                Row (modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ){
                                    TableCellPerkembanganBalita(umur, false, Modifier.weight(1f))
                                    TableCellPerkembanganBalita(nilai, false, Modifier.weight(1f))
                                    TableCellPerkembanganBalita(nilai2, false, Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TableCellPerkembanganBalita(text: String, isHeader: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.padding(horizontal = 8.dp),
        style = if (isHeader) MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
        else MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Start
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewInformasiPerkembanganBalitaScreen() {
//    InformasiPerkembanganBalitaScreen()
}
