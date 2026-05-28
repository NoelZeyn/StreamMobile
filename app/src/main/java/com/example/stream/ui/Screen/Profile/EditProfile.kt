package com.example.stream.ui.Screen.Profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.stream.Data.Local.UserPreferences
import com.example.stream.Data.Model.Request.PortalProfileRequest
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: ProfilViewModel,
    navController: NavController
) {
    val updateState = viewModel.updateState
    val profileState by viewModel.profileState.collectAsState()

    val nama = remember { mutableStateOf("") }
    val nik = remember { mutableStateOf("") }
//    val tanggalLahir = remember { mutableStateOf("") }
    val nomorTelepon = remember { mutableStateOf("") }
    val jenisKelamin = remember { mutableStateOf("") }
    val tanggalLahir = remember { mutableStateOf<LocalDate?>(null) }
    val tanggalLahirString = remember { mutableStateOf("") }


    val listJenisKelamin = listOf("Laki-laki", "Perempuan")
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val token by UserPreferences.getToken(context).collectAsState(initial = "")
    val id by UserPreferences.getUserId(context).collectAsState(initial = 0)

    LaunchedEffect(Unit) {
        if (!token.isNullOrEmpty()) {
            val bearerToken = "Bearer $token"
            id?.let { viewModel.getProfile(bearerToken) }
        }
    }

    LaunchedEffect(profileState) {
        if(profileState is GetProfileState.Success) {
            val profile = (profileState as GetProfileState.Success).data
            nama.value = profile.name ?: ""
            nik.value = profile.nik ?: ""
            nomorTelepon.value = profile.channel_name ?: ""
            jenisKelamin.value = profile.jenis_kelamin ?: ""
            profile.tanggal_lahir?.let {
                tanggalLahir.value = LocalDate.parse(it)
                tanggalLahirString.value = tanggalLahir.value!!.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            }
        } else {}
    }

    when (updateState) {
        is UpdateProfileState.Loading -> {
            CircularProgressIndicator()
        }
        is UpdateProfileState.Success -> {
            Toast.makeText(context, "Perubahan telah disimpan", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
        is UpdateProfileState.Error -> {
            Toast.makeText(context, (updateState as UpdateProfileState.Error).message, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
        else -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ){
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Kembali",
                modifier = Modifier.padding(end = 15.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "Edit Profil",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 1.dp)
            )
        }

        when(profileState) {
            is GetProfileState.Success -> {
                val profile = (profileState as GetProfileState.Success).data
                CustomFormField(
                    label = "Nama",
                    value = nama.value,
                    onValueChange = { nama.value = it },
                    placeholder = "Nama lengkap sesuai KTP",
                    trailingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                CustomFormField(
                    label = "NIK",
                    value = nik.value,
                    onValueChange = { nik.value = it },
                    placeholder = "Masukkan NIK Anda",
                    trailingIcon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(12.dp))

                val dateDialogState = rememberMaterialDialogState()

                MaterialDialog(
                    dialogState = dateDialogState,
                    buttons = {
                        positiveButton(text = "Ok")
                        negativeButton(text = "Cancel")
                    }
                ) {
                    datepicker(
                        initialDate = tanggalLahir.value ?: LocalDate.now(),
                        title = "Pilih Tanggal"
                    ) { date ->
                        tanggalLahir.value = date
                        tanggalLahirString.value = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                        Toast.makeText(
                            context,
                            "Tanggal dipilih: ${tanggalLahirString.value}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                CustomFormField(
                    label = "Tanggal Lahir",
                    value = tanggalLahirString.value,
                    onValueChange = {},
                    placeholder = "Pilih tanggal lahir Anda",
                    trailingIcon = {
                        IconButton(onClick = {
                            dateDialogState.show()
                        }) {
                            Icon(Icons.Default.DateRange, contentDescription = null)

                        }
                    },
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                CustomFormField(
                    label = "Nomor Telepon",
                    value = nomorTelepon.value,
                    onValueChange = { nomorTelepon.value = it },
                    placeholder = "Nomor telepon aktif",
                    trailingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Jenis Kelamin Dropdown
                Text(
                    text = "Jenis Kelamin",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = jenisKelamin.value,
                        onValueChange = { },
                        readOnly = true,
                        placeholder = { Text("Pilih jenis kelamin Anda", color = Color.Gray) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(16.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color(0xFFF1F1F1),
                            unfocusedContainerColor = Color(0xFFF1F1F1),
                            disabledContainerColor = Color(0xFFF1F1F1)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listJenisKelamin.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    jenisKelamin.value = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

            }
            is GetProfileState.Error -> {
                val message = (profileState as GetProfileState.Error).message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Gagal memuat profil: $message", color = Color.Red)
                }
            }

            else -> {}
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (!token.isNullOrEmpty()) {
                    val userId = id
                    val bearerToken = "Bearer $token"
                    val request = PortalProfileRequest(
                        name = nama.value,
                        nik = nik.value,
                        tanggal_lahir = tanggalLahir.value?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "",
                        channel_name = nomorTelepon.value,
                        jenis_kelamin = jenisKelamin.value
                    )
                    if (userId != null) {
                        viewModel.updateProfile(request, userId, bearerToken)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005F6B)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Simpan", color = Color.White)
        }

        when(updateState) {
            is UpdateProfileState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is UpdateProfileState.Success -> {
                LaunchedEffect(Unit) {
                    viewModel.resetState()
                }
                Text("Berhasil memperbarui profil!", color = Color.Green)
            }
            is UpdateProfileState.Error -> {
                Text("Error: ${(updateState as UpdateProfileState.Error).message}", color = Color.Red)
            }
            else -> {}
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


@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    MaterialTheme {
//        EditProfileScreen()
    }
}
