package com.example.stream.ui.Screen.AnggotaKeluarga

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stream.Data.Model.Request.JenisKelamin
import com.example.stream.Data.Model.Request.PosisiKeluarga
import com.example.stream.ui.Screen.components.HeaderBackground
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.rememberScrollState

@Composable
fun CompleteAnggotaKeluargaScreen(
    navController: NavController,
    viewModel: AnggotaKeluargaViewModel,
    onSuccessAnggotaKeluargaState: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var nik by remember { mutableStateOf("") }
    var nama by remember { mutableStateOf("") }
    var anak_ke by remember { mutableStateOf(0) }
    var jenisKelamin by remember { mutableStateOf<JenisKelamin?>(null) }
    var tanggalLahir by remember { mutableStateOf(LocalDate.now()) }

    val posisi = viewModel.posisi_keluarga

    LaunchedEffect(posisi) {
        if (posisi == PosisiKeluarga.ISTRI) {
            anak_ke = 0
        }
    }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(tanggalLahir)
        }
    }

    val anggotaKeluargaState by viewModel.anggotaKeluargaState.collectAsState()

    var lastAction by remember { mutableStateOf("") }

    fun saveAndGoLogin() {
        coroutineScope.launch {
            if (jenisKelamin != null && nik.isNotBlank()) {
                viewModel.saveAnggotaKeluarga(nama, nik, tanggalLahir, jenisKelamin!!, anak_ke)
                viewModel.save()
                lastAction = "login"
            }
        }
    }

    fun saveAndBackToList() {
        coroutineScope.launch {
            if (jenisKelamin != null && nik.isNotBlank()) {
                viewModel.saveAnggotaKeluarga(nama, nik, tanggalLahir, jenisKelamin!!, anak_ke)
                viewModel.save()
                lastAction = "back"
            }
        }
    }

    val no_kk = viewModel.no_kk

    LaunchedEffect(anggotaKeluargaState) {
        if (anggotaKeluargaState is AnggotaKeluargaState.Success) {
            when (lastAction) {
                "login" -> navController.navigate("Login")
                "back" -> navController.navigate("anggota/$no_kk")
            }
        }
    }

    CompleteAnggotaKeluargaContent(
        nik = nik,
        onNikChange = { nik = it },
        nama = nama,
        onNama = { nama = it },
        anak_ke = anak_ke,
        onAnak_ke = { anak_ke = it },
        jenisKelamin = jenisKelamin,
        onJenisKelamin = { jenisKelamin = it },
        tanggalLahir = tanggalLahir,
        onTanggalLahir = { tanggalLahir = it },
        posisi = posisi,
        anggotaKeluargaState = anggotaKeluargaState,
        onSaveAndGoLogin = { saveAndGoLogin() },
        onSaveAndBackToList = { saveAndBackToList() },
        onSuccessAnggotaKeluargaState = onSuccessAnggotaKeluargaState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CompleteAnggotaKeluargaContent(
    nik: String,
    onNikChange: (String) -> Unit,
    nama: String,
    onNama: (String) -> Unit,
    anak_ke: Int,
    onAnak_ke: (Int) -> Unit,
    jenisKelamin: JenisKelamin?,
    onJenisKelamin: (JenisKelamin) -> Unit,
    tanggalLahir: LocalDate,
    onTanggalLahir: (LocalDate) -> Unit,
    posisi: PosisiKeluarga,
    anggotaKeluargaState: AnggotaKeluargaState,
    onSaveAndGoLogin: () -> Unit,
    onSaveAndBackToList: () -> Unit,
    onSuccessAnggotaKeluargaState: () -> Unit
) {
    val options = listOf("Laki-laki", "Perempuan")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember {
        mutableStateOf(
            when (jenisKelamin) {
                JenisKelamin.LAKI -> "Laki-laki"
                JenisKelamin.PEREMPUAN -> "Perempuan"
                else -> ""
            }
        )
    }

    // Format tanggal tampil di TextField
    val tanggalLahir = remember { mutableStateOf<LocalDate?>(null) }
    val tanggalLahirString = remember { mutableStateOf("") }

    // Date Picker Dialog
    val dateDialogState = rememberMaterialDialogState()

    //Deklarasi posko
    var expandedPosko by remember { mutableStateOf(false) }
    var poskoPosyandu by remember { mutableStateOf("") }

    val poskoList = listOf("Posko A", "Posko B", "Posko C")

    val context = LocalContext.current

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
            Toast.makeText(context, "Tanggal dipilih: ${tanggalLahirString.value}", Toast.LENGTH_SHORT).show()
        }
    }

    val scrollState = rememberScrollState()

    CompleteAnggotaKeluargaHeader()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 170.dp)
            .verticalScroll(scrollState)
    ) {
        //card
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
                CustomFormField(
                    label = "NIK",
                    value = nik,
                    onValueChange = onNikChange,
                    placeholder = "Masukkan NIK",
                    keyboardType = KeyboardType.Number,
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Icon person")
                    }

                )
                Spacer(modifier = Modifier.height(16.dp))

                CustomFormField(
                    label = "Nama",
                    value = nama,
                    onValueChange = onNama,
                    placeholder = "Masukkan nama lengkap",
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Icon person")
                    }

                )
                Spacer(modifier = Modifier.height(16.dp))

                // Jenis Kelamin Dropdown
                Text(
                    text = "Jenis Kelamin",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, bottom = 4.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = selectedOptionText,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Pilih Jenis Kelamin") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF1F1F1)),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color(0xFFF1F1F1),
                            unfocusedContainerColor = Color(0xFFF1F1F1)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedOptionText = option
                                    expanded = false
                                    onJenisKelamin(
                                        when (option) {
                                            "Laki-laki" -> JenisKelamin.LAKI
                                            "Perempuan" -> JenisKelamin.PEREMPUAN
                                            else -> return@DropdownMenuItem
                                        }
                                    )
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Label Tanggal Lahir
//                Text(
//                    text = "Tanggal Lahir",
//                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 4.dp)
//                )

                // TextField untuk menampilkan tanggal yang dipilih
//                TextField(
//                    value = tanggalLahirText,
//                    onValueChange = {}, // read-only, jadi tidak mengubah nilai langsung
//                    readOnly = true,
//                    trailingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.CalendarToday,
//                            contentDescription = "Pilih tanggal"
//                        )
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(16.dp))
//                        .background(Color(0xFFF1F1F1))
//                        .clickable { dateDialogState.show() },
//                    placeholder = { Text("Pilih Tanggal Lahir") },
//                    colors = TextFieldDefaults.colors(
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
//                        disabledIndicatorColor = Color.Transparent,
//                        focusedContainerColor = Color(0xFFF1F1F1),
//                        unfocusedContainerColor = Color(0xFFF1F1F1),
//                        disabledContainerColor = Color(0xFFF1F1F1)
//                    )
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
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

                Spacer(modifier = Modifier.height(16.dp))

                // lanjutan dari file sebelumnya
                // anak ke
                if (posisi != PosisiKeluarga.ISTRI) {
                    CustomFormField(
                        label = "Anak ke-",
                        value = if (anak_ke == 0) "" else anak_ke.toString(),
                        onValueChange = { newValue ->
                            newValue.toIntOrNull()?.let { onAnak_ke(it) }
                        },
                        placeholder = "Masukkan urutan anak",
                        keyboardType = KeyboardType.Number,
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Icon person")
                        }

                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Label teks "Posko Posyandu"
//                Text(
//                    text = "Posko Posyandu",
//                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 4.dp)
//                )
//
//                // TextField yang tampil sebagai input read-only untuk menampilkan pilihan posko
//                TextField(
//                    value = poskoPosyandu,  // teks yang tampil, misalnya "Posko A"
//                    onValueChange = {},      // karena read-only, tidak ada perubahan input manual
//                    readOnly = true,
//                    placeholder = { Text("Pilih Posko Posyandu") },  // teks placeholder jika belum ada pilihan
//                    trailingIcon = {
//                        IconButton(onClick = { expandedPosko = !expandedPosko }) {
//                            Icon(
//                                imageVector = Icons.Default.ArrowDropDown,
//                                contentDescription = "Toggle dropdown"
//                            )
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(16.dp))
//                        .background(Color(0xFFF1F1F1)),
//                    colors = TextFieldDefaults.colors(
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
//                        disabledIndicatorColor = Color.Transparent,
//                        focusedContainerColor = Color(0xFFF1F1F1),
//                        unfocusedContainerColor = Color(0xFFF1F1F1),
//                        disabledContainerColor = Color(0xFFF1F1F1)
//                    )
//                )

                // Dropdown menu yang muncul saat icon dropdown diklik
                DropdownMenu(
                    expanded = expandedPosko,          // apakah dropdown sedang terbuka
                    onDismissRequest = { expandedPosko = false },  // aksi ketika dropdown ditutup
                    modifier = Modifier.fillMaxWidth()
                ) {
                    poskoList.forEach { posko ->
                        DropdownMenuItem(
                            text = { Text(posko) },
                            onClick = {
                                poskoPosyandu = posko    // simpan pilihan posko
                                expandedPosko = false    // tutup dropdown setelah pilih
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                //button
                if (anggotaKeluargaState is AnggotaKeluargaState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = onSaveAndBackToList,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF005F6B),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Daftarkan")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

//header
@Composable
fun CompleteAnggotaKeluargaHeader() {
    HeaderBackground {
        Text(
            text = "Anak", //anak/ibu/ayah
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Masukkan data anggota keluargamu yang akan terdaftar di layanan Stream Management.",
            fontSize = 14.sp,
            color = Color.White
        )
    }
}

@Composable
fun CustomFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    trailingIcon: @Composable (() -> Unit)? = null, // ikon di kanan
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
            trailingIcon = trailingIcon, // ikon ditampilkan di kanan
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

@Preview(showBackground = true)
@Composable
fun CompleteAnggotaKeluargaPreview() {
    CompleteAnggotaKeluargaContent(
        nik = "",
        onNikChange = {},
        nama = "",
        onNama = {},
        anak_ke = 0,
        onAnak_ke = {},
        jenisKelamin = null,
        onJenisKelamin = {},
        tanggalLahir = LocalDate.now(),
        onTanggalLahir = {},
        posisi = PosisiKeluarga.ANAK,
        anggotaKeluargaState = AnggotaKeluargaState.Idle,
        onSaveAndBackToList = {},
        onSaveAndGoLogin = {},
        onSuccessAnggotaKeluargaState = {}
    )
}
