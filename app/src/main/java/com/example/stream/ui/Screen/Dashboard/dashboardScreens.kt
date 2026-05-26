package com.example.stream.ui.Screen.Login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stream.Data.Local.UserPreferences
import com.example.stream.Data.Model.Response.PortalBeritaItem
import com.example.stream.Data.Model.Response.PosyanduDetailResponse
import com.example.stream.R
import com.example.stream.ui.Screen.Berita.JadwalMingguIniSection
import com.example.stream.ui.Screen.Berita.PortalBeritaViewModel
import com.example.stream.ui.Screen.Berita.UiState
import com.example.stream.ui.Screen.Berita.formatTanggalIndonesia
import com.example.stream.ui.Screen.Profile.GetProfileState
import com.example.stream.ui.Screen.Profile.PosyanduState
import com.example.stream.ui.Screen.Profile.ProfilViewModel
import com.example.stream.ui.Screen.components.MainScaffold
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun   LoginScreenTest(
    navController: NavController,
    portalBeritaViewModel: PortalBeritaViewModel,
    profilViewModel: ProfilViewModel
) {
    HeaderBackground()

    val context = LocalContext.current
    val token by UserPreferences.getToken(context).collectAsState(initial = "")
    val no_kk by UserPreferences.getNoKK(context).collectAsState(initial = "")
    val id by UserPreferences.getUserId(context).collectAsState(initial = 0)

    val beritaState by portalBeritaViewModel.uiState.collectAsState()
    val profileState by profilViewModel.profileState.collectAsState()
    val posyanduState by profilViewModel.posyanduState.collectAsState()

    LaunchedEffect(Unit) {
        val bearerToken = "Bearer $token"
        portalBeritaViewModel.fetchBerita(bearerToken, no_kk)
        id?.let { profilViewModel.getProfile(bearerToken, it) }
        no_kk?.let { token?.let { it1 -> profilViewModel.fetchPosyandu(it, it1) } }
    }

    MainScaffold(navController = navController, currentRoute = "dashboard") { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            HeaderBackground()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                HelpDeskIcon(onClick = {}, navController = navController)

                when (profileState) {
                    is GetProfileState.Loading -> Text("Memuat....")
                    is GetProfileState.Success -> {
                        val data = (profileState as GetProfileState.Success).data
                        HeaderContent(data.name ?: "-")
                    }
                    else -> {}
                }

                Spacer(modifier = Modifier.height(16.dp))
                MenuSection(navController)
                Spacer(modifier = Modifier.height(16.dp))
                ImageSlider() //kode baru

                when (beritaState) {
                    is UiState.Loading -> Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                    is UiState.Error -> {
                        Spacer(modifier = Modifier.height(16.dp))
                        JadwalKosongCard()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    is UiState.Success<*> -> {
                        val data = (beritaState as UiState.Success<List<PortalBeritaItem>>).data

                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val today = LocalDate.now()

                        val upComingEvent = data
                            .mapNotNull {
                                try {
                                    val date = LocalDate.parse(it.tanggal, formatter)
                                    if (date.isAfter(today)) it to date else null
                                } catch (e: Exception) {
                                    null
                                }
                            }
                            .minByOrNull { it.second }
                            ?.first

                        if (upComingEvent != null) {
                            JadwalMingguIniSection(
                                judul = upComingEvent.judul,
                                location = upComingEvent.tempat.orEmpty(),
                                date = formatTanggalIndonesia(upComingEvent.tanggal),
                                onDetailClick = {
                                    val id = upComingEvent.id
                                    navController.navigate("berita-detail/$id")
                                }
                            )
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                            JadwalKosongCard()
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                when (posyanduState) {
                    is PosyanduState.Loading -> {
                        // tampilkan loading, misal ProgressBar
                    }
                    is PosyanduState.Success -> {
                        val data = (posyanduState as PosyanduState.Success).data
                        Log.d("  LoginScreenTest", "Posyandu data: $data")
                        InformasiPosko(nama = data.nama_posyandu, alamat = data.alamat, keluarga = data.jumlah_keluarga)
                    }
                    is PosyanduState.Error -> {
                        // tampilkan pesan error
                    }
                    else -> {
                        // opsional, kalau ada case lain
                    }
                }

            }
        }
    }
}

@Composable
fun HeaderBackground() {
    val context = LocalContext.current
    val imageBitmap = ImageBitmap.imageResource(context.resources, R.drawable.headertekstur)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF08607A),
                        Color(0xFF84BBD1)
                    )
                )
            )
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun HeaderContent(nama: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar_woman),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Hallo $nama",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Selamat Datang di Stream Management!",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Pantau kesehatan keluarga & info penting seputar ibu & anak!",
            fontSize = 12.sp,
            color = Color.White
        )
    }
}

@Composable
fun HelpDeskIcon(
    modifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
    onClick: () -> Unit,
    navController: NavController
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.SupportAgent,
            contentDescription = "Helpdesk",
            modifier = Modifier
                .size(iconSize)
                .clickable { navController.navigate("Help-Desk") },
            tint = Color.White
        )
    }
}

@Composable
fun MenuSection(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MenuItem(R.drawable.portalperiksa, "Portal Periksa", Modifier.weight(1f), Color(0xFFE3F2FD)) {
            navController.navigate("portal-periksa")
        }
        MenuItem(R.drawable.portalberita, "Portal Berita", Modifier.weight(1f), Color(0xFFFFF9C4)) {
            navController.navigate("berita")
        }
        MenuItem(R.drawable.ekms, "E-KMS", Modifier.weight(1f), Color(0xFFFFE4E9)) {
            navController.navigate("riwayat-ekms")
        }
    }
}

@Composable
fun MenuItem(
    drawableResId: Int,
    label: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF0F0F0),
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = drawableResId),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun JadwalKosongCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF8F9FA))
            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Jadwal Minggu Ini",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF08607A)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Belum ada jadwal kegiatan posyandu untuk minggu ini.",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ImageSlider(
    imageList: List<Int> = listOf(
        R.drawable.slider1,
        R.drawable.slider2,
        R.drawable.slider3,
        R.drawable.slider4
    ),
    slideDurationMillis: Long = 3000L
) {
    val pagerState = rememberPagerState()

    // Auto slide dengan coroutine
    LaunchedEffect(Unit) {
        while (true) {
            delay(slideDurationMillis)
            val nextPage = (pagerState.currentPage + 1) % imageList.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            count = imageList.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            Image(
                painter = painterResource(id = imageList[page]),
                contentDescription = "Image $page",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}


@Composable
fun InformasiPosko(nama: String, alamat: String, keluarga: Int) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "Informasi Posko",
            color = Color(0xFF074A5D),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White), // background putih
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // shadow lebih nyata
        ) {
            Column {
                // Header dengan background gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF08607A), Color(0xFF84BBD1))
                            )
                        )
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = nama,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }

                // Konten dengan icon + text
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Alamat",
                            tint = Color(0xFF08607A),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Alamat", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(68.dp))
                        Text(": $alamat", fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Kader",
                            tint = Color(0xFF08607A),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Jumlah Keluarga", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(9.dp))
                        Text(": $keluarga", fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun   LoginScreenTestPreview() {
    //   LoginScreenTest()
}
