package com.example.stream.ui.Screen.Login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.stream.R
import com.example.stream.ui.Screen.Register.FormInput

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel,
) {
    val email by viewModel.email
    val password by viewModel.password
    val captchaInput by viewModel.captchaInput
    val captchaUrl by viewModel.captchaUrl.collectAsState()

    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current
    val showErrorToast by viewModel.showErrorToast.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        LaunchedEffect(loginState) {
            if (loginState is LoginState.Success) {
                navController.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }

        LoginContent(
            email = email,
            onEmailChange = { viewModel.email.value = it },
            password = password,
            onPasswordChange = { viewModel.password.value = it },
            captchaUrl = captchaUrl,
            captchaInput = captchaInput,
            onCaptchaInputChange = { viewModel.captchaInput.value = it },
            onRefreshCaptcha = { viewModel.loadCaptcha() },
            onNext = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Email dan Password wajib diisi!", Toast.LENGTH_SHORT).show()
                } else if (captchaInput.isEmpty()) {
                    Toast.makeText(context, "Kode Keamanan (Captcha) wajib diisi!", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.login(context)
                }
            },
            onNextMore = {
                navController.navigate("onboarding")
            }
        )

        if (loginState is LoginState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF005F6B))
            }
        }

        LaunchedEffect(showErrorToast) {
            if (showErrorToast) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                viewModel.resetErrorToast()
            }
        }
    }
}

@Composable
fun LoginContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    captchaUrl: String,
    captchaInput: String,
    onCaptchaInputChange: (String) -> Unit,
    onRefreshCaptcha: () -> Unit,
    onNext: () -> Unit = {},
    onNextMore: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(58.dp))

        Text(
            text = "Streamer Management",
            color = Color(0xFF005F6B),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Selamat Datang",
            color = Color(0xFF005F6B),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Silakan lengkapi formulir berikut untuk masuk",
            color = Color(0xFF7D7F81),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        CustomFormField(
            label = "Alamat Email",
            value = email,
            onValueChange = onEmailChange,
            placeholder = "email@perusahaan.com",
            trailingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        var passwordVisible by remember { mutableStateOf(false) }

        FormInput(
            label = "Password",
            value = password,
            onValueChange = onPasswordChange,
            placeholder = "••••••••",
            keyboardType = KeyboardType.Password,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (passwordVisible) "Sembunyikan kata sandi" else "Tampilkan kata sandi"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Kode Keamanan (Captcha)",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Color(0xFFE8E9ED), shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = captchaUrl,
                    contentDescription = "Captcha",
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

        Spacer(modifier = Modifier.height(10.dp))

        CustomFormField(
            label = "Masukkan Kode Keamanan",
            value = captchaInput,
            onValueChange = onCaptchaInputChange,
            placeholder = "MASUKKAN KODE CAPTCHA",
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005F6B)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("MASUK KE SISTEM", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "Belum memiliki Akun?")
            TextButton(onClick = onNextMore) {
                Text("Daftar", color = Color(0xFFFF9800), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
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
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE8E9ED), shape = RoundedCornerShape(10.dp)),
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

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginContent(
        email = "",
        onEmailChange = {},
        password = "",
        onPasswordChange = {},
        captchaUrl = "",
        captchaInput = "",
        onCaptchaInputChange = {},
        onRefreshCaptcha = {}
    )
}