package com.example.stream.ui.Screen.Berita

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarViewDay
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Place

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import com.example.stream.Data.Local.UserPreferences
import com.example.stream.Data.Model.Response.PortalBeritaItem
import com.example.stream.ui.Screen.Login.JadwalKosongCard
import com.example.stream.ui.Screen.components.MainScaffold
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun PortalBeritaScreen(
    navController: NavController,
    viewModel: PortalBeritaViewModel,
) {
    val context = LocalContext.current
    val token by UserPreferences.getToken(context).collectAsState(initial = "")
    val posyanduId by UserPreferences.getNoKK(context).collectAsState(initial = "")

    LaunchedEffect(Unit) {
        val bearerToken = "Bearer $token"
        viewModel.fetchBerita(bearerToken, posyanduId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    MainScaffold(
        navController = navController,
        currentRoute = "berita"
    ) {
        paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Portal Berita",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0A1D2D)
                )
            }

            when (uiState) {
                is UiState.Loading -> Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                is UiState.Error -> {
                    JadwalKosongCard()
                }
                is UiState.Success<*> -> {
                    val data = (uiState as UiState.Success<List<PortalBeritaItem>>).data
                    if (data.isEmpty()) {
                        Text("Belum ada berita.")
                    } else {
                        val today = LocalDate.now()
                        val beritaSekarang = data.filter {
                            val tanggal = parseLocalDateFromString(it.tanggal)
                            tanggal == null || !tanggal.isAfter(today)
                        }

                        val beritaMendatang = data.filter {
                            val tanggal = parseLocalDateFromString(it.tanggal)
                            tanggal != null && tanggal.isAfter(today)
                        }

                        if (beritaMendatang.isNotEmpty()) {
                            Text("Akan Datang", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            beritaMendatang.forEach {
                                NewsCard(
                                    title = it.judul,
                                    location = it.tempat ?: "-",
                                    date = formatTanggalIndonesia(it.tanggal),
                                    backgroundColor = Color(0xFFE1F1F8),
                                    buttonColor = Color(0xFFFFA800),
                                    onDetailClick = {
                                        navController.navigate("berita-detail/${it.id}")
                                    }
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Text("Telah Lewat", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        beritaSekarang.forEach {
                            NewsCard(
                                title = it.judul,
                                location = it.tempat ?: "-",
                                date = formatTanggalIndonesia(it.tanggal),
                                backgroundColor = Color(0xFFF0F2F2), // grey-ish background
                                buttonColor = Color.Gray,
                                onDetailClick = {
                                    navController.navigate("berita-detail/${it.id}")
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsCard(
    title: String,
    location: String,
    date: String,
    backgroundColor: Color,
    buttonColor: Color,
    onDetailClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0A1D2D))
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Place,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = location, fontSize = 14.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarViewDay,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = date,
                        fontSize = 14.sp,
                        color = if (buttonColor == Color.Gray) Color.Gray else Color(0xFF2773E0)
                    )
                }
            }

            Button(
                onClick = onDetailClick,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Detail", fontSize = 12.sp, color = Color.White)
            }
        }
    }
}

fun formatTanggalIndonesia(tanggal: String): String {
    val formats = listOf(
        "dd-MM-yyyy",
        "yyyy-MM-dd",
        "dd/MM/yyyy"
    )

    for (format in formats) {
        try {
            val inputFormat = SimpleDateFormat(format, Locale.US)
            inputFormat.isLenient = false
            val date = inputFormat.parse(tanggal)
            if (date != null) {
                val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                return outputFormat.format(date)
            }
        } catch (_: Exception) {
        }
    }

    return tanggal
}

fun parseLocalDateFromString(dateString: String): LocalDate? {
    val formatters = listOf(
        DateTimeFormatter.ofPattern("dd-MM-yyyy"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    )

    for (formatter in formatters) {
        try {
            return LocalDate.parse(dateString, formatter)
        } catch (_: Exception) { }
    }
    return null
}

@Composable
fun JadwalMingguIniSection(
    judul: String,
    location: String,
    date: String,
    onDetailClick: () -> Unit = {}
) {
    Column {
        Text(
            text = "Jadwal Posyandu Minggu Ini",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        NewsCard(
            title = judul,
            location = location,
            date = date,
            backgroundColor = Color(0xFFE1F1F8),
            buttonColor = Color(0xFFFFA800),
            onDetailClick = onDetailClick
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPortalBerita() {
//    PortalBeritaScreen()
}
