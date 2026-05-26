package com.example.stream.ui.Screen.Register//package com.example.stream.ui.Screen.Register
//
////import androidx.compose.ui.text.input.PasswordVisualTransformation
////import androidx.compose.ui.draw.clip
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.wrapContentSize
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material.icons.filled.CalendarToday
//import androidx.compose.material.icons.filled.DateRange
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.onGloballyPositioned
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.stream.Data.Model.Request.JenisKelamin
//import com.example.stream.Data.Model.Response.PosyanduItem
//import com.example.stream.ui.Screen.components.HeaderBackground
//import com.vanpra.composematerialdialogs.MaterialDialog
//import com.vanpra.composematerialdialogs.datetime.date.datepicker
//import com.vanpra.composematerialdialogs.rememberMaterialDialogState
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//import androidx.compose.ui.unit.IntSize
//
//@Composable
//fun CompleteDataScreen(
//    navController: NavController,
//    viewModel: RegisterViewModel = viewModel(),
//    onSuccessRegister: () -> Unit
//) {
//    val context = LocalContext.current
//    val nik by viewModel.nik.collectAsState()
//    val no_kk by viewModel.no_kk.collectAsState()
//    var tanggalLahir by remember { mutableStateOf(LocalDate.now()) }
//    var jenisKelamin by remember { mutableStateOf<JenisKelamin?>(null) }
//    var poskoPosyandu by remember { mutableStateOf<PosyanduItem?>(null) }
//
//    val password = viewModel.password
//    val registerState by viewModel.registerState.collectAsState()
//
//    var hasNavigated by remember { mutableStateOf(false) }
//
//
//    LaunchedEffect(Unit) {
//        viewModel.posko()
//    }
//
//    LaunchedEffect(registerState) {
//        if (!hasNavigated) {
//            when (registerState) {
//                is RegisterState.Success -> {
//                    hasNavigated = true
//                    navController.navigate("anggota/$no_kk") {
//                        popUpTo("complete_data") { inclusive = true }
//                    }
//                }
//                is RegisterState.Error -> {
//                    val message = (registerState as RegisterState.Error).error
//                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
//                }
//                else -> {}
//            }
//        }
//    }
//
//    when (registerState) {
//        is RegisterState.Loading -> {
//            CircularProgressIndicator(modifier = Modifier
//                .fillMaxSize()
//                .wrapContentSize(Alignment.Center))
//        }
//        else -> {}
//    }
//    CompleteDataContent(
//        nik = nik,
//        onNikChange = { newNik ->
//            viewModel.saveNik(newNik)
//        },
//        no_kk = no_kk,
//        onNo_kk = { newNo_kk ->
//            viewModel.saveNoKk(newNo_kk)
//        },
//        jenisKelamin = jenisKelamin,
//        onJenisKelamin = { jenisKelamin = it },
//        tanggalLahir = tanggalLahir,
//        onTanggalLahir = { tanggalLahir = it },
//        poskoPosyandu = poskoPosyandu,
//        onPoskoPosyanduChange = { poskoPosyandu = it },
//        registerState = registerState,
//        onNext = {
//            if (jenisKelamin == null || poskoPosyandu == null || password == null) {
//                Toast.makeText(context, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
//                return@CompleteDataContent
//            }
//
//            viewModel.saveCompleteData(nik, no_kk, jenisKelamin!!, tanggalLahir, poskoPosyandu!!.id )
//            viewModel.register(password.value, context)
//        },
//        onSuccessRegister = onSuccessRegister,
//        viewModel = viewModel
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CompleteDataContent(
//    nik: String,
//    onNikChange: (String) -> Unit,
//    no_kk: String,
//    onNo_kk: (String) -> Unit,
//    jenisKelamin: JenisKelamin?,
//    onJenisKelamin: (JenisKelamin) -> Unit,
//    tanggalLahir: LocalDate,
//    onTanggalLahir: (LocalDate) -> Unit,
//    poskoPosyandu: PosyanduItem?,
//    onPoskoPosyanduChange: (PosyanduItem) -> Unit,
//    registerState: RegisterState,
//    onNext: () -> Unit = {},
//    onSuccessRegister: () -> Unit = {},
//    viewModel: RegisterViewModel
//) {
//    CompleteDataHeader()
//
//    var expandedJenisKelamin by remember { mutableStateOf(false) }
//    var expandedPosko by remember { mutableStateOf(false) }
//
////    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
//
//    val tanggalLahir = remember { mutableStateOf<LocalDate?>(null) }
//    val tanggalLahirString = remember { mutableStateOf("") }
//
//
//    val poskoList by viewModel.poskoState.collectAsState()
//
//    val dateDialogState = rememberMaterialDialogState()
//
//    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }
//
//    val context = LocalContext.current
//
//
//
//    MaterialDialog(
//        dialogState = dateDialogState,
//        buttons = {
//            positiveButton(text = "Ok")
//            negativeButton(text = "Cancel")
//        }
//    ) {
//        datepicker(
//            initialDate = tanggalLahir.value ?: LocalDate.now(),
//            title = "Pilih Tanggal"
//        ) { date ->
//            tanggalLahir.value = date
//            tanggalLahirString.value = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
//            Toast.makeText(context, "Tanggal dipilih: ${tanggalLahirString.value}", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(top = 170.dp)
//    ) {
//        Card(
//            modifier = Modifier
//                .fillMaxSize()
//                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
//            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White)
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(24.dp)
//                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState()),
//                verticalArrangement = Arrangement.Top,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                // Input NIK
//                CustomFormField(
//                    label = "No. KK",
//                    value = no_kk,
//                    onValueChange = onNo_kk,
//                    placeholder = "Masukkan NIK"
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//
//
//                CustomFormField(
//                    label = "NIK",
//                    value = nik,
//                    onValueChange = onNikChange,
//                    placeholder = "Masukkan NIK"
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = "Jenis Kelamin",
//                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 4.dp)
//                )
//
//                Box (modifier = Modifier
//                    .fillMaxWidth()
//                    .onGloballyPositioned { coordinates ->
//                        textFieldSize = coordinates.size
//                    }
//                ){
//                    TextField(
//                        value = jenisKelamin?.name ?: "",
//                        onValueChange = {},
//                        readOnly = true,
//                        placeholder = { Text("Pilih Jenis Kelamin") },
//                        trailingIcon = {
//                            IconButton(onClick = { expandedJenisKelamin = !expandedJenisKelamin }) {
//                                Icon(
//                                    imageVector = Icons.Default.ArrowDropDown,
//                                    contentDescription = "Toggle dropdown"
//                                )
//                            }
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clip(RoundedCornerShape(16.dp))
//                            .background(Color(0xFFF1F1F1)),
//                        colors = TextFieldDefaults.colors(
//                            focusedIndicatorColor = Color.Transparent,
//                            unfocusedIndicatorColor = Color.Transparent,
//                            disabledIndicatorColor = Color.Transparent,
//                            focusedContainerColor = Color(0xFFF1F1F1),
//                            unfocusedContainerColor = Color(0xFFF1F1F1),
//                            disabledContainerColor = Color(0xFFF1F1F1)
//                        )
//                    )
//
//                    DropdownMenu(
//                        expanded = expandedJenisKelamin,
//                        onDismissRequest = { expandedJenisKelamin = false },
//                        modifier = Modifier
//                            .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
//                    ) {
//                        JenisKelamin.entries.forEach { jenis ->
//                            DropdownMenuItem(
//                                text = { Text(jenis.name) },
//                                onClick = {
//                                    onJenisKelamin(jenis)
//                                    expandedJenisKelamin = false
//                                },
//                                modifier = Modifier.background(Color.White)
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Tanggal Lahir Picker
////                Text(
////                    text = "Tanggal Lahir",
////                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .padding(bottom = 4.dp)
////                )
//
////                TextField(
////                    value = tanggalLahirText,
////                    onValueChange = {},
////                    readOnly = true,
////                    trailingIcon = {
////                        Icon(
////                            imageVector = Icons.Default.CalendarToday,
////                            contentDescription = "Select date"
////                        )
////                    },
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .clip(RoundedCornerShape(16.dp))
////                        .background(Color(0xFFF1F1F1))
////                        .clickable {
////                            dateDialogState.show()
////                        },
////                    placeholder = { Text("Pilih Tanggal Lahir") },
////                    colors = TextFieldDefaults.colors(
////                        focusedIndicatorColor = Color.Transparent,
////                        unfocusedIndicatorColor = Color.Transparent,
////                        disabledIndicatorColor = Color.Transparent,
////                        focusedContainerColor = Color(0xFFF1F1F1),
////                        unfocusedContainerColor = Color(0xFFF1F1F1),
////                        disabledContainerColor = Color(0xFFF1F1F1)
////                    )
////                )
//                CustomFormField(
//                    label = "Tanggal Lahir",
//                    value = tanggalLahirString.value,
//                    onValueChange = {},
//                    placeholder = "Pilih tanggal lahir Anda",
//                    trailingIcon = {
//                        IconButton(onClick = {
//                            dateDialogState.show()
//                        }) {
//                            Icon(Icons.Default.DateRange, contentDescription = null)
//                        }
//                    },
//                    readOnly = true
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = "Posko Posyandu",
//                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 4.dp)
//                )
//
//                Box (modifier = Modifier
//                    .fillMaxWidth()
//                    .onGloballyPositioned { coordinates ->
//                        textFieldSize = coordinates.size
//                    }
//                ) {
//
//                    TextField(
//                        value = poskoPosyandu?.nama ?: "",
//                        onValueChange = {},
//                        readOnly = true,
//                        placeholder = { Text("Pilih Posko Posyandu") },
//                        trailingIcon = {
//                            IconButton(onClick = { expandedPosko = !expandedPosko }) {
//                                Icon(
//                                    imageVector = Icons.Default.ArrowDropDown,
//                                    contentDescription = "Toggle dropdown"
//                                )
//                            }
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clip(RoundedCornerShape(16.dp))
//                            .background(Color(0xFFF1F1F1)),
//                        colors = TextFieldDefaults.colors(
//                            focusedIndicatorColor = Color.Transparent,
//                            unfocusedIndicatorColor = Color.Transparent,
//                            disabledIndicatorColor = Color.Transparent,
//                            focusedContainerColor = Color(0xFFF1F1F1),
//                            unfocusedContainerColor = Color(0xFFF1F1F1),
//                            disabledContainerColor = Color(0xFFF1F1F1)
//                        )
//                    )
//
//                    DropdownMenu(
//                        expanded = expandedPosko,
//                        onDismissRequest = { expandedPosko = false },
//                        modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
//                    ) {
//                        when (poskoList) {
//                            is PoskoState.Success -> {
//                                val poskoData = (poskoList as PoskoState.Success).poskoList
//                                if (poskoData.isEmpty()) {
//                                    DropdownMenuItem(
//                                        text = { Text("Tidak ada data posyandu") },
//                                        onClick = {}
//                                    )
//                                } else {
//                                    poskoData.forEach { posko ->
//                                        DropdownMenuItem(
//                                            text = { Text(posko.nama) },
//                                            onClick = {
//                                                onPoskoPosyanduChange(posko)
//                                                expandedPosko = false
//                                            }
//                                        )
//                                    }
//                                }
//                            }
//                            is PoskoState.Loading -> {
//                                DropdownMenuItem(
//                                    text = { Text("Memuat...") },
//                                    onClick = {}
//                                )
//                            }
//                            is PoskoState.Error -> {
//                                DropdownMenuItem(
//                                    text = {
//                                        Text(
//                                            (poskoList as PoskoState.Error).error.take(50) + "..."
//                                        )
//                                    },
//                                    onClick = {}
//                                )
//                            }
//                            else -> {}
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                Button(
//                    onClick = onNext,
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005F6B)),
//                    shape = RoundedCornerShape(50),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(48.dp)
//                ) {
//                    Text("Selanjutnya", color = Color.White)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun CompleteDataHeader() {
//    HeaderBackground {
//        Text(
//            text = "Verifikasi Identitas Anda",
//            fontWeight = FontWeight.Bold,
//            fontSize = 18.sp,
//            color = Color.White
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            text = "Masukkan data yang sesuai agar proses registrasi berjalan lancar.",
//            fontSize = 14.sp,
//            color = Color.White
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun CompleteDataPreview() {
//    val dummyPosko = PosyanduItem(id = 0, nama = "Posko Contoh")
//
//    val dummyViewModel = object : RegisterViewModel() {}
//
//    CompleteDataContent(
//        nik = "",
//        onNikChange = {},
//        no_kk = "",
//        onNo_kk = {},
//        jenisKelamin = null,
//        onJenisKelamin = {},
//        tanggalLahir = LocalDate.now(),
//        onTanggalLahir = {},
//        poskoPosyandu = dummyPosko,
//        onPoskoPosyanduChange = {},
//        registerState = RegisterState.Idle,
//        viewModel = dummyViewModel
//    )
//}
