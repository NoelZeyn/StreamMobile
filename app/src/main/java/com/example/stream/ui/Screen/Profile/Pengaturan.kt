package com.example.stream.ui.Screen.Profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController


@Composable
fun PengaturanScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header dengan ikon kembali dan judul
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Kembali",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Pengaturan Akun",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0A1D2D) // warna teks gelap
            )
        }

        // Kartu pengaturan
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Baris Email
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Email",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Ubah Email",
                        color = Color(0xFFFF9800), // warna oranye
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { navController.navigate("edit-email") }
                    )
                }

                Divider(color = Color.LightGray, thickness = 0.5.dp)

                // Baris Password
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Password",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Ubah Password",
                        color = Color(0xFFFF9800),
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {
                            navController.navigate("Update-Password")
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PengaturanScreenPreview() {
    MaterialTheme {
//        PengaturanScreen()

    }
}