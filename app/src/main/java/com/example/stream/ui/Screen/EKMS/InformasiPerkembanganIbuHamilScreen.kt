package com.example.stream.ui.Screen.EKMS

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InformasiPerkembanganIbuHamilScreen() {
    val dataPerkembangan = listOf(
        listOf("01-01-2025", "1", "50 Kg", "25 cm", "120/80"),
        listOf("01-02-2025", "1", "51 Kg", "26 cm", "110/70"),
        listOf("01-03-2025", "2", "53 Kg", "27 cm", "115/75"),
        listOf("01-04-2025", "2", "55 Kg", "28 cm", "118/78"),
        listOf("01-05-2025", "3", "58 Kg", "29 cm", "120/80"),
        listOf("01-06-2025", "3", "60 Kg", "30 cm", "122/82")
    )

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Informasi Perkembangan Ibu Hamil",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007BFF)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(15.dp))
                    .border(1.dp, Color(0xFFE5E5E5), RoundedCornerShape(15.dp))
                    .clip(RoundedCornerShape(15.dp))
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFCCE5FF))
                        .height(44.dp)
                ) {
                    listOf("Tanggal", "Trimester", "BB", "LILA", "Tensi").forEach {
                        TableCellPerkembanganIbuHamil(
                            text = it,
                            isHeader = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Data Rows
                dataPerkembangan.forEachIndexed { index, row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (index % 2 == 0) Color(0xFFF9F9F9) else Color.White)
                            .height(40.dp)
                    ) {
                        row.forEach {
                            TableCellPerkembanganIbuHamil(
                                text = it,
                                isHeader = false,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TableCellPerkembanganIbuHamil(
    text: String,
    isHeader: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = if (isHeader) {
                MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            } else {
                MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
            },
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInformasiPerkembanganIbuHamilScreen() {
    InformasiPerkembanganIbuHamilScreen()
}
