package com.example.stream.ui.Screen.Register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
// FIX: Import untuk AsyncImage Coil
import coil.compose.AsyncImage
import com.example.stream.ui.Screen.components.HeaderBackground

@Composable
fun PasswordScreen(
    navController: NavController,
    viewModel: RegisterViewModel,
) {
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    val captchaUrl by viewModel.captchaUrl.collectAsState()
    val captchaInput by viewModel.captchaInput.collectAsState()

    val context = LocalContext.current
    val registerState by viewModel.registerState.collectAsState()
    var hasNavigated by remember { mutableStateOf(false) }

    LaunchedEffect(registerState) {
        if (!hasNavigated) {
            when (registerState) {
                is RegisterState.Success -> {
                    hasNavigated = true
                    Toast.makeText(context, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                    navController.navigate("login") {
                        popUpTo("password") { inclusive = true }
                    }
                }
                is RegisterState.Error -> {
                    val message = (registerState as RegisterState.Error).error
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PasswordContent(
            password = password,
            onPasswordChange = { password = it },
            repeatPassword = repeatPassword,
            onRepeatPasswordChange = { repeatPassword = it },
            captchaUrl = captchaUrl,
            captchaInput = captchaInput,
            onCaptchaInputChange = { viewModel.captchaInput.value = it },
            onRefreshCaptcha = { viewModel.loadCaptcha() },
            onNext = {
                if (password.isEmpty()) {
                    Toast.makeText(context, "Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                    return@PasswordContent
                }
                if (captchaInput.isEmpty()) {
                    Toast.makeText(context, "Captcha wajib diisi!", Toast.LENGTH_SHORT).show()
                    return@PasswordContent
                }

                if (password == repeatPassword) {
                    viewModel.savePassword(password)
                    viewModel.register(context)
                } else {
                    Toast.makeText(context, "Password baru tidak cocok!", Toast.LENGTH_LONG).show()
                }
            }
        )

        if (registerState is RegisterState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                color = Color(0xFF005F6B)
            )
        }
    }
}

@Composable
fun PasswordContent(
    password: String,
    onPasswordChange: (String) -> Unit,
    repeatPassword: String,
    onRepeatPasswordChange: (String) -> Unit,
    captchaUrl: String,
    captchaInput: String,
    onCaptchaInputChange: (String) -> Unit,
    onRefreshCaptcha: () -> Unit,
    onNext: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var oldPasswordVisible by remember { mutableStateOf(false) }

    HeaderBackground {
        Text(
            text = "Bikin Password yang Aman",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Gunakan kombinasi huruf, angka, dan simbol untuk meningkatkan perlindungan.",
            fontSize = 14.sp,
            color = Color.White
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 170.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FormInput(
                    label = "Password Baru",
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholder = "Masukkan password baru",
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                FormInput(
                    label = "Ulangi Password",
                    value = repeatPassword,
                    onValueChange = onRepeatPasswordChange,
                    placeholder = "Ulangi password",
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (oldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { oldPasswordVisible = !oldPasswordVisible }) {
                            Icon(imageVector = if (oldPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Verifikasi Keamanan",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AsyncImage(
                            model = captchaUrl,
                            contentDescription = "Captcha Image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(vertical = 4.dp)
                        )

                        IconButton(onClick = onRefreshCaptcha) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Captcha",
                                tint = Color(0xFF005F6B)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                FormInput(
                    label = "Masukkan Kode Keamanan",
                    value = captchaInput,
                    onValueChange = onCaptchaInputChange,
                    placeholder = "MASUKKAN KODE",
                    keyboardType = KeyboardType.Text
                )

                Spacer(modifier = Modifier.height(34.dp))

                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005F6B)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("SIMPAN & DAFTAR", color = Color.White)
                }
            }
        }
    }
}

// FIX: Menambahkan kembali fungsi FormInput Composable yang hilang
@Composable
fun FormInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            trailingIcon = trailingIcon,
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(16.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = Color(0xFFF1F1F1),
                unfocusedContainerColor = Color(0xFFF1F1F1),
                disabledContainerColor = Color(0xFFF1F1F1)
            )
        )
    }
}