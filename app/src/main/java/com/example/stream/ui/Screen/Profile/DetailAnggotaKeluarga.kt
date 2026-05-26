package com.example.stream.ui.Screen.Profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.stream.Data.Local.UserPreferences
import com.example.stream.Data.Model.Request.PortalProfileAnggotaRequest
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EditFamilyMemberActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            EditFamilyMemberScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFamilyMemberScreen(
    navController: NavController,
    viewModel: ProfilViewModel,
    oldNik: String
) {
    val context = LocalContext.current

    val name = remember { mutableStateOf("") }
    val nik = remember { mutableStateOf("") }
    val status = remember { mutableStateOf("") }

    val birthDate = remember { mutableStateOf("") }
    val tanggalLahir = remember { mutableStateOf<LocalDate?>(null) }

    val gender = remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val token by UserPreferences.getToken(context).collectAsState(initial = "")
    val id by UserPreferences.getUserId(context).collectAsState(initial = 0)

    val profileState by viewModel.profileAnggotaState.collectAsState()
    val anggotaProfileState by viewModel.updateAnggotaState.collectAsState()

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
            birthDate.value = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            Toast.makeText(context, "Tanggal dipilih: ${birthDate.value}", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!token.isNullOrEmpty()) {
            val bearerToken = "Bearer $token"
            viewModel.getAnggotaProfile(bearerToken, oldNik)
        }
    }

    LaunchedEffect(profileState) {
        if (profileState is GetAnggotaProfileState.Success) {
            val profile = (profileState as GetAnggotaProfileState.Success).data
            name.value = profile.nama_anggota_keluarga ?: "-"
            nik.value = profile.nik ?: "-"
            status.value = profile.posisi_keluarga ?: "-"
            birthDate.value = profile.tanggal_lahir ?: "-"
            tanggalLahir.value = try {
                LocalDate.parse(profile.tanggal_lahir, DateTimeFormatter.ofPattern("dd MMM yyyy"))
            } catch (e: Exception) {
                null
            }
            gender.value = profile.jenis_kelamin ?: "-"
        }
    }

    when(anggotaProfileState) {
        is UpdateProfileState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = Color.Blue
            )
        }
        is UpdateProfileState.Success -> {
            Toast.makeText(context, "Profil anggota berhasil diperbarui", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
        is UpdateProfileState.Error -> {
            val message = (anggotaProfileState as UpdateProfileState.Error).message
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
        else -> {}
    }

    val genderOptions = listOf("Laki-laki", "Perempuan")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Informasi Anggota Keluarga") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when(profileState) {
                is GetAnggotaProfileState.Loading -> Text("Sedang memuat data...")
                is GetAnggotaProfileState.Success -> {
                    FieldWithIcon(
                        value = name.value,
                        onValueChange = { name.value = it },
                        label = "Nama",
                        placeholder = "Nama lengkap sesuai KTP",
                        icon = Icons.Default.Person
                    )

                    FieldWithIcon(
                        value = nik.value,
                        onValueChange = { nik.value = it },
                        label = "NIK",
                        placeholder = "Masukkan NIK Anda",
                        icon = Icons.Default.DoneOutline
                    )

                    FieldWithIcon(
                        value = status.value,
                        onValueChange = { status.value = it },
                        label = "Status",
                        placeholder = "Pilih status anggota",
                        icon = Icons.Default.Dataset
                    )

                    CustomFormField(
                        label = "Tanggal Lahir",
                        value = birthDate.value,
                        onValueChange = { birthDate.value = it },
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

//                    FieldWithIcon(
//                        value = birthDate.value,
//                        onValueChange = { birthDate.value = it },
//                        label = "Tanggal Lahir",
//                        placeholder = "Pilih tanggal lahir Anda",
//                        icon = Icons.Default.CalendarToday
//                    )

                    Text(text = "Jenis Kelamin", fontWeight = FontWeight.SemiBold)
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = gender.value,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Pilih jenis kelamin Anda") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFE5E5EA),
                                unfocusedContainerColor = Color(0xFFE5E5EA)

                            ),

                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            genderOptions.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        gender.value = selectionOption
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
                else -> {}
            }

            Button(
                onClick = {
                    if (!token.isNullOrEmpty()) {
                        val userId = id
                        val bearerToken = "Bearer $token"
                        val request = PortalProfileAnggotaRequest(
                            nama_anggota_keluarga = name.value,
                            nik = nik.value,
                            posisi_keluarga = status.value,
                            tanggal_lahir = tanggalLahir.value?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "",
                            jenis_kelamin = gender.value
                        )
                        if (userId != null) {
                            viewModel.updateAnggotaProfile(request, oldNik, bearerToken)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00597A),
                    contentColor = Color.White
                )
            ) {
                Text("Simpan", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FieldWithIcon(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: ImageVector
) {
    Column {
        Text(text = label, fontWeight = FontWeight.SemiBold)
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            trailingIcon = { Icon(icon, contentDescription = null) },
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE5E5EA),
                unfocusedContainerColor = Color(0xFFE5E5EA),
                focusedIndicatorColor = Color.Transparent,     // hilangkan garis saat fokus
                unfocusedIndicatorColor = Color.Transparent,   // hilangkan garis saat tidak fokus
                disabledIndicatorColor = Color.Transparent     // hilangkan garis saat disabled
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditFamilyMemberScreen() {
//    EditFamilyMemberScreen()
}
