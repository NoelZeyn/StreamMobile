package com.example.stream.ui.Screen.Profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.stream.Data.Local.UserPreferences
import com.example.stream.Data.Model.Request.UpdateEmailRequest

@Composable
fun UbahEmailScreen(
    navController: NavController,
    viewModel: ProfilViewModel
) {
    var emailBaru by remember { mutableStateOf("") }
    var emailSekarang by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val updateEmailState = viewModel.updateEmailState
    val context = LocalContext.current
    val token by UserPreferences.getToken(context).collectAsState(initial = "")
    val id by UserPreferences.getUserId(context).collectAsState(initial = 0)

    LaunchedEffect(updateEmailState) {
        when (val state = updateEmailState) {
            is UpdateEmailState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetEmailState()
            }
            is UpdateEmailState.Success -> {
                Toast.makeText(context, state.data.message, Toast.LENGTH_SHORT).show()
                viewModel.resetEmailState()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                text = "Ubah Email",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0A1D2D)
            )
        }

        // Deskripsi
        Text(
            text = "Ganti email akun Anda untuk terus menerima info penting dan notifikasi.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Email Saat Ini
        Text(
            text = "Email saat ini",
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = emailSekarang,
            onValueChange = { emailSekarang = it },
            placeholder = { Text("Masukkan email saat ini") },
//            readOnly = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFE5E5EA),
                focusedContainerColor = Color(0xFFE5E5EA)
            ),
//            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp)
        )

        // Email Baru
        Text(
            text = "Email baru",
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = emailBaru,
            onValueChange = { emailBaru = it },
            placeholder = { Text("Masukkan Email baru Anda") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFE5E5EA),
                focusedContainerColor = Color(0xFFE5E5EA)
            )
        )

        // Password Konfirmasi
        Text(
            text = "Masukkan kata sandi akun untuk konfirmasi perubahan.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        var passwordVisible by remember { mutableStateOf(false) }

        Text(
            text = "Kata Sandi",
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Masukkan kata sandi Anda") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFE5E5EA),
                focusedContainerColor = Color(0xFFE5E5EA)
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (passwordVisible) "Sembunyikan kata sandi" else "Tampilkan kata sandi"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )

        // Tombol Simpan & Batal
        Button(
            onClick = {
                if (!token.isNullOrEmpty()) {
                    val userId = id
                    val bearerToken = "Bearer $token"
                    val request = UpdateEmailRequest(
                        email_baru = emailBaru,
                        email_sekarang = emailSekarang,
                        password = password
                    )
                    if (userId != null) {
                        viewModel.updateEmail(request, userId, bearerToken)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005B71))
        ) {
            Text("Simpan Perubahan", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF005B71))
        ) {
            Text("Batal", fontWeight = FontWeight.Bold)
        }

        when(updateEmailState) {
            is UpdateEmailState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is UpdateEmailState.Success -> {
                LaunchedEffect(Unit) {
                    viewModel.resetState()
                }
                Text("Berhasil memperbarui email!", color = Color.Green)
            }
            is UpdateEmailState.Error -> {
                Text("Error: ${(updateEmailState as UpdateEmailState.Error).message}", color = Color.Red)
            }
            else -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UbahEmailScreenPreview() {
//    UbahEmailScreen()
}
