package com.example.stream.ui.Screen.Profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.rotate
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
                FAQScreen(navController)
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
            answer = "Aplikasi ini dirancang untuk memudahkan manajemen alur kerja, pemantauan status layanan secara real-time, dan pendaftaran antrian operasional langsung dari perangkat Anda."
        ),
        FAQItem(
            question = "Di mana saya bisa melihat hasil pemantauan atau laporan?",
            answer = "Masuk ke menu Portal Pemeriksaan, lalu pilih kategori atau proyek yang ingin Anda lihat untuk memantau data perkembangan, metrik utama, dan status terbarunya."
        ),
        FAQItem(
            question = "Bagaimana cara mendaftar antrian di Stream Management?",
            answer = "Masuk ke menu Portal Berita, temukan jadwal stream atau sesi yang tersedia."
        ),
        FAQItem(
            question = "Apakah data saya tersimpan dengan aman?",
            answer = "Tenang, semua data operasional Anda tersimpan dengan aman di enkripsi sistem dan hanya dapat diakses oleh Anda serta tim admin Stream Management yang berwenang."
        ),
        FAQItem(
            question = "Apakah aplikasi ini bisa diakses tanpa koneksi internet?",
            answer = "Beberapa data yang telah dimuat sebelumnya dapat dilihat secara offline, namun untuk pembaruan data masuk, pendaftaran antrian, dan sinkronisasi tetap memerlukan koneksi internet."
        )
    )

    var expandedIndex by remember { mutableStateOf(0) }

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
    val backgroundColor by animateColorAsState(
        targetValue = if (isExpanded) Color(0xFFFFFFFF) else Color(0xFFE6F8FE),
        label = "CardBackgroundAnimation"
    )

    val arrowRotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "ArrowRotationAnimation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
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
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    modifier = Modifier.rotate(arrowRotationState)
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
        val navController = rememberNavController()
        FAQScreen(navController)
    }
}