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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stream.R

data class BoardingPage(val title: String, val description: String, val image: Int)

@Composable
fun BoardingScreen(onStartClick: () -> Unit) {
    val pages = listOf(
        BoardingPage("Stream Management", "Bantu streamer lebih mudah, cepat, dan terintegrasi", R.drawable.onboarding1),
        BoardingPage("Data Tercatat Aman", "Pencatatan pertumbuhan anak dan ibu tersimpan rapi dan mudah diakses", R.drawable.onboarding2),
        BoardingPage("Gabung Sekarang", "Daftar dan mulai gunakan Stream Management sekarang juga!", R.drawable.onboarding3)
    )

    var currentPage by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        BoardingContent(page = pages[currentPage])

        DotIndicator(currentPage = currentPage, totalPages = pages.size)

        if (currentPage == pages.lastIndex) {
            ButtonBar(onStartClick)
        } else {
            ButtonRound {
                currentPage++
            }
        }
    }
}

@Composable
fun BoardingContent(page: BoardingPage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = page.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = page.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF08607A)
        )
        Text(
            text = page.description,
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp),
            lineHeight = 22.sp,
            textAlign = TextAlign.Center
        )
    }
}

//dot indicator, petunjuk page
@Composable
fun DotIndicator(currentPage: Int, totalPages: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        repeat(totalPages) { index ->
            val color = if (index == currentPage) Color(0xFF08607A) else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(10.dp)
                    .background(color, CircleShape)
            )
        }
    }
}


//button bar untuk page3
@Composable
fun ButtonBar(onStartClick: () -> Unit) {
    Button(
        onClick = onStartClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(Color(0xFF08607A))
    ) {
        Text("Mulai", color = Color.White, fontSize = 16.sp)
    }
}


//button round next page 1,2
@Composable
fun ButtonRound(onNextClick: () -> Unit) {
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
                .clickable { onNextClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next",
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoardingScreen() {
BoardingScreen {

}
}
