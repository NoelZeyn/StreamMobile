package com.example.stream.ui.Screen.Register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stream.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel,
) {
    val email by viewModel.email.collectAsState()
    val name by viewModel.name.collectAsState()
    val channel_name by viewModel.channel_name.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        RegisterContent(
            email = email,
            onEmailChange = { newEmail ->
                viewModel.saveRegisterInfo(newEmail, name, channel_name)
            },
            name = name,
            onNameChange = { newName ->
                viewModel.saveRegisterInfo(email, newName, channel_name)
            },
            channel_name = channel_name,
            onChannelChange = { newChannel ->
                viewModel.saveRegisterInfo(email, name, newChannel)
            },
            onNext = {
                coroutineScope.launch {
                    println("Email: $email, Name: $name, Name Channel: $channel_name")
                    viewModel.saveRegisterInfo(email, name, channel_name)
                    delay(100)
                    navController.navigate("password")
                }
            },
            onNextMore = {
                navController.navigate("login")
            }
        )
    }
}

@Composable
fun RegisterContent(
    email: String,
    onEmailChange: (String) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    channel_name: String,
    onChannelChange: (String) -> Unit,
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(58.dp))

            Text(
                text = "Stream Management",
                color = Color(0xFF005F6B),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Selamat Datang",
            color = Color(0xFF005F6B),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            ),
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Silakan lengkapi formulir berikut untuk memulai",
            color = Color(0xFF7D7F81),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        CustomFormField(
            label = "Email",
            value = email,
            onValueChange = onEmailChange,
            placeholder = "Masukkan Email Anda",
            trailingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomFormField(
            label = "Nama",
            value = name,
            onValueChange = onNameChange,
            placeholder = "Nama Panggilan",
            trailingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomFormField(
            label = "Nama Channel",
            value = channel_name,
            onValueChange = onChannelChange,
            placeholder = "Nama Channel",
            trailingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005F6B)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Selanjutnya", color = Color.White)
        }

        Spacer(modifier = Modifier.height(120.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Sudah memiliki akun?")
            TextButton(onClick = onNextMore) {
                Text("Masuk", color = Color(0xFFFF9800))
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
fun RegisterScreenPreview() {
    val navController = rememberNavController()
    val dummyViewModel = RegisterViewModel()
    RegisterScreen(
        navController = navController,
        viewModel = dummyViewModel,
    )
}