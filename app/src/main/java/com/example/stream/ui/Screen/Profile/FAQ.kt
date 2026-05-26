package com.example.stream.ui.Screen.Profile

//@file:OptIn(ExperimentalMaterial3Api::class)

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class FAQActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            MaterialTheme {
                FAQScreen( navController )
            }
        }
    }
}

data class FAQItem(
    val question: String,
    val answer: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(
    navController: NavController
) {
    val faqItems = listOf(
        FAQItem(
            question = "Apa itu aplikasi Stream Management?",
            answer = "Aplikasi ini memudahkan orang tua memantau pertumbuhan anak, melihat hasil pemeriksaan, dan mendaftar antrian Posyandu langsung dari HP."
        ),
        FAQItem("Di mana saya bisa melihat hasil pemeriksaan anak?", "Masuk ke menu Portal Pemeriksaan, lalu pilih nama anak untuk melihat data berat badan, tinggi, dan status gizi."),
        FAQItem("Kapan data pemeriksaan muncul?", "Data akan muncul setelah pemeriksaan selesai dan kader menginput datanya ke sistem."),
        FAQItem("Bagaimana cara mendaftar antrian Posyandu?", "Masuk ke Portal Berita, temukan pengumuman jadwal, lalu tekan tombol “Daftar Antrian”."),
        FAQItem("Tidak bisa daftar antrian, kenapa ya?", "Pastikan Anda sudah login dan jadwalnya masih tersedia. Jika tetap gagal, hubungi kader Posyandu."),
        FAQItem("Data saya aman nggak?", "Beberapa data yang sudah tersimpan bisa dilihat offline, tapi untuk input baru dan antrian butuh koneksi internet."),
        FAQItem("Apakah aplikasi bisa dipakai tanpa internet?", "Tenang, semua data tersimpan aman dan hanya bisa diakses oleh Anda dan kader Posyandu.")
    )

    var expandedIndex by remember { mutableStateOf(0) } // default buka item pertama

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "FAQ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Temukan jawaban atas pertanyaan umum seputar penggunaan aplikasi Stream Management di bawah ini.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LazyColumn {
                items(faqItems.size) { index ->
                    FAQCard(
                        item = faqItems[index],
                        isExpanded = expandedIndex == index,
                        onClick = {
                            expandedIndex = if (expandedIndex == index) -1 else index
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun FAQCard(item: FAQItem, isExpanded: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded) Color(0xFFFFFFFF) else Color(0xFFE6F8FE)
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.question,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ArrowBack else Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }

            if (isExpanded && item.answer.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.answer,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewFAQScreen() {
    MaterialTheme {
//        FAQScreen()
    }
}
