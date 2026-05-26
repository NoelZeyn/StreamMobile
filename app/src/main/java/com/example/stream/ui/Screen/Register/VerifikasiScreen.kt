package com.example.stream.ui.Screen.Register

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stream.ui.Screen.components.HeaderBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt



@Composable
fun VerifikasiScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()   // fillMaxSize supaya column penuh layar
                .padding(paddingValues)
                .imePadding()
        ) {
            // Card hanya mengisi sisa ruang di bawah header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),  // agar card isi sisa space di bawah header
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.offset(y = (-10).dp)) { // offset konten ke atas 10dp
                    VerifikasiContent(snackbarHostState, coroutineScope)
                }
            }
        }
    }
}


@Composable
fun VerifikasiHeader() {
    HeaderBackground {
        Text(
            text = "Verifikasi Kode OTP",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Kami telah mengirimkan kode verifikasi ke email kamu. Cek inbox atau folder spam apabila pesan belum muncul.",
            fontSize = 14.sp,
            color = Color.White
        )
    }
}

@Composable
fun VerifikasiContent(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    var resendTime by remember { mutableStateOf(59) }
    var otpCode by remember { mutableStateOf(List(6) { "" }) }

    val offsetX = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (resendTime > 0) {
            delay(1000L)
            resendTime--
        }
    }

    suspend fun shakeEffect() {
        val vibration = listOf(-16f, 16f, -12f, 12f, -8f, 8f, 0f)
        for (x in vibration) {
            offsetX.animateTo(x, animationSpec = tween(durationMillis = 50))
        }
    }
    VerifikasiHeader()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 170.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //isi content

                Text(
                    text = "Masukkan 6 digit kode yang telah dikirim ke email Anda.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Box(modifier = Modifier.offset { IntOffset(offsetX.value.roundToInt(), 0) }) {
                    VerifikasiBox(otpCode) { index, value ->
                        if (value.length <= 1 && value.all { it.isDigit() }) {
                            otpCode = otpCode.toMutableList().also { it[index] = value }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val isComplete = otpCode.all { it.isNotEmpty() }
                        coroutineScope.launch {
                            if (!isComplete) {
                                shakeEffect()
                                snackbarHostState.showSnackbar("Kode OTP belum lengkap!")
                            } else {
                                snackbarHostState.showSnackbar("Kode berhasil dikirim (simulasi)!")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005F73))
                ) {
                    Text(text = "Verifikasi", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (resendTime > 0) {
                    Text(
                        text = "Kirim ulang dalam 00:${
                            resendTime.toString().padStart(2, '0')
                        } detik",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Tidak mendapatkan kode OTP?")
                        TextButton(onClick = { resendTime = 59 }) {
                            Text("Kirim Ulang", color = Color(0xFFFFA726))
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun VerifikasiBox(
    otpCode: List<String>,
    onValueChange: (Int, String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        otpCode.forEachIndexed { index, digit ->
            OutlinedTextField(
                value = digit,
                onValueChange = { value -> onValueChange(index, value) },
                modifier = Modifier
                    .width(48.dp)
                    .height(56.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun VerifikasiScreenPreview() {
    VerifikasiScreen()
}






