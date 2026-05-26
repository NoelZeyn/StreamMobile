package com.example.stream.ui.Screen.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stream.R

data class OnBoardingPage(
    val title: String,
    val description: String,
    val image: Int
)

@Composable
fun OnBoardingScreen(
    navController: NavController
) {
    val pages = listOf(
        OnBoardingPage(
            title = "Stream Management",
            description = "Bantu streamer lebih mudah, cepat, dan terintegrasi",
            image = R.drawable.onboarding1
        ),
        OnBoardingPage(
            title = "Cek Jadwal & Antrian",
            description = "Lihat jadwal stream dan ambil nomor antrian mabar.",
            image = R.drawable.onboarding2
        ),
        OnBoardingPage(
            title = "Data Tersimpan Aman",
            description = "Semua data streamer tersimpan dengan aman dan dapat diakses kapan saja.",
            image = R.drawable.onboarding3
        )
    )

    var currentPage by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = pages[currentPage].image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        // Dot indicator
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        ) {
            pages.forEachIndexed { index, _ ->
                val color = if (index == currentPage) Color(0xFF08607A) else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(10.dp)
                        .background(color, CircleShape)
                )
            }
        }

        // Title & Description
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = pages[currentPage].title,
                fontSize = 22.sp,
                color = Color(0xFF08607A),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = pages[currentPage].description,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp),
                lineHeight = 22.sp
            )
        }

        // Next Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFF08607A), CircleShape)
                    .clickable {
                        if (currentPage < pages.lastIndex) {
                            currentPage++
                        } else {
                            navController.navigate("email")
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Next",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun onBoardingPage3() {
    OnBoardingPage(
        title = "Data Tersimpan Aman",
        description = "Semua data perkembangan anak tersimpan dengan aman dan dapat diakses kapan saja.",
        image = R.drawable.onboarding3
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewOnboardingScreen() {
//    onBoardingScreen()
}

