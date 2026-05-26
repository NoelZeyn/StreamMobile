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
import com.example.stream.Data.Model.Request.UpdatePasswordRequest

@Composable
fun UbahPasswordScreen(
    navController: NavController,
    viewModel: ProfilViewModel
) {
    val updatePasswordState = viewModel.updatePasswordState
    val context = LocalContext.current

    val token by UserPreferences.getToken(context).collectAsState(initial = "")
    val id by UserPreferences.getUserId(context).collectAsState(initial = 0)

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
    
    LaunchedEffect(updatePasswordState) {
        when (val state = viewModel.updatePasswordState) {
            is UpdatePasswordState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetPasswordState()
            }
            is UpdatePasswordState.Success -> {
                Toast.makeText(context, state.data.message, Toast.LENGTH_SHORT).show()
                viewModel.resetPasswordState()
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
                text = "Ubah Password",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0A1D2D)
            )
        }

        // Deskripsi
        Text(
            text = "Amankan akun kamu dengan mengganti password secara berkala.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        var oldPasswordVisible by remember { mutableStateOf(false) }
        var newPasswordVisible by remember { mutableStateOf(false) }

        // Kata Sandi Saat Ini
        Text("Kata sandi saat ini", fontWeight = FontWeight.Medium)
        TextField(
            value = oldPassword,
            onValueChange = { oldPassword = it },
            placeholder = { Text("Masukkan kata sandi saat ini") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFE5E5EA),
                focusedContainerColor = Color(0xFFE5E5EA)
            ),
            visualTransformation = if (oldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (oldPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (oldPasswordVisible) "Sembunyikan kata sandi" else "Tampilkan kata sandi"
                IconButton(onClick = { oldPasswordVisible = !oldPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )

        // Kata Sandi Baru
        Text("Kata sandi baru", fontWeight = FontWeight.Medium)
        TextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            placeholder = { Text("Min. 8 karakter, masukkan huruf dan angka") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFE5E5EA),
                focusedContainerColor = Color(0xFFE5E5EA)
            ),
            visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (newPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (newPasswordVisible) "Sembunyikan kata sandi" else "Tampilkan kata sandi"
                IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )

//        // Ulangi Kata Sandi Baru
//        Text("Ulangi kata sandi baru", fontWeight = FontWeight.Medium)
//        TextField(
//            value = confirmPassword,
//            onValueChange = { confirmPassword = it },
//            placeholder = { Text("Pastikan sama dengan di atas") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            shape = RoundedCornerShape(12.dp),
//            colors = TextFieldDefaults.colors(
//                unfocusedContainerColor = Color(0xFFE5E5EA),
//                focusedContainerColor = Color(0xFFE5E5EA)
//            )
//        )

        Spacer(modifier = Modifier.height(200.dp))

        // Tombol Simpan
        Button(
            onClick = {
                if (!token.isNullOrEmpty()) {
                    val userId = id
                    val bearerToken = "Bearer $token"
                    val request = UpdatePasswordRequest(
                        password_baru = newPassword,
                        password_sekarang = oldPassword,
                    )
                    if (userId != null) {
                        viewModel.updatePassword(request, userId, bearerToken)
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

        // Tombol Batal
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
    }
}

@Preview(showBackground = true)
@Composable
fun UbahPasswordScreenPreview() {
//    UbahPasswordScreen()
}
