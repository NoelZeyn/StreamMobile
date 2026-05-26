package com.example.stream.ui.Screen.EKMS


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RiwayatSuplemenTambahanScreen() {
    val riwayatSuplemenTambahan = listOf(
        Triple("1 Bulan", "Hepatitis B", "Posko Melati"),
        Triple("2 Bulan", "BCG", "Posko Melati"),
        Triple("3 Bulan", "DPT-HB-Hib 1", "Posko Melati"),
        Triple("4 Bulan", "Polio 2", "Posko Melati"),
        Triple("5 Bulan", "DPT-HB-Hib 2", "Posko Melati"),
        Triple("6 Bulan", "Campak", "Posko Melati")
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Riwayat Suplemen Tambahan",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF007BFF)
        )

        Spacer(modifier = Modifier.height(8.dp))

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
                    TableCellSuplemen("Umur", true, Modifier.weight(1f))
                    TableCellSuplemen("Jenis Imunisasi", true, Modifier.weight(1f))
                    TableCellSuplemen("Posko", true, Modifier.weight(1f))
                }
                Divider()

                riwayatSuplemenTambahan.forEach { (umur, jenis, posko) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TableCellSuplemen(umur, false, Modifier.weight(1f))
                        TableCellSuplemen(jenis, false, Modifier.weight(1f))
                        TableCellSuplemen(posko, false, Modifier.weight(1f))
                    }
                    Divider()
                }
            }
        }
    }
}

@Composable
fun TableCellSuplemen(text: String, isHeader: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        style = if (isHeader) MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
        else MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center
    )
}



@Preview(showBackground = true)
@Composable
fun PreviewRiwayatSuplemenTambahanScreen() {
    RiwayatSuplemenTambahanScreen()
}
