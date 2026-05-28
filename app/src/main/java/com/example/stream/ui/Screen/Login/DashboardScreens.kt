package com.example.stream.ui.Screen.Dashboard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.navigation.compose.rememberNavController
import com.example.stream.Data.Local.UserPreferences
import com.example.stream.Data.Model.Response.PortalBeritaItem
import com.example.stream.ui.Screen.Berita.JadwalMingguIniSection
import com.example.stream.ui.Screen.Berita.PortalBeritaViewModel
import com.example.stream.ui.Screen.Berita.UiState
import com.example.stream.ui.Screen.Berita.formatTanggalIndonesia
import com.example.stream.ui.Screen.Profile.GetProfileState
import com.example.stream.ui.Screen.Profile.PosyanduState
import com.example.stream.ui.Screen.Profile.ProfilViewModel
import com.example.stream.ui.Screen.components.MainScaffold
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreens(    navController: NavController,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dashboard Mini",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF005F6B)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F7FA))
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon Indikator Berhasil
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home Icon",
                tint = Color(0xFF005F6B),
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Teks Status Login Sukses
            Text(
                text = "🎉 Login Berhasil!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF005F6B),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kamu telah berhasil diarahkan ke halaman Dashboard dari LoginScreen.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Tombol Kembali/Logout untuk ngetest balik ke Login
            Button(
                onClick = {
                    // Navigasi balik ke halaman login dan hapus stack history
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("LOGOUT / KEMBALI", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DashboardScreensPreview() {
    DashboardScreens(navController = rememberNavController())
}