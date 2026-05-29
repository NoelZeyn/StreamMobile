package com.example.stream.ui.Screen.Dashboard

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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.stream.Data.Local.UserPreferences
import com.example.stream.Data.Model.Response.LiveScheduleItem
import com.example.stream.Data.Model.Response.ScheduleItem
import com.example.stream.R
import com.example.stream.ui.Screen.Profile.GetProfileState
import com.example.stream.ui.Screen.Profile.ProfilViewModel
import com.example.stream.ui.Screen.components.MainScaffold
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen(
    navController: NavController,
//    profilViewModel: ProfilViewModel,
    dashboardViewModel: DashboardViewModel
) {
    val context = LocalContext.current
    val token by UserPreferences.getToken(context).collectAsState(initial = "")
    val id by UserPreferences.getUserId(context).collectAsState(initial = 0)

//    val profileState by profilViewModel.profileState.collectAsState()
    val scheduleState by dashboardViewModel.scheduleState.collectAsState()
    val liveState by dashboardViewModel.liveState.collectAsState()

    LaunchedEffect(id, token) {
        if (token?.isNotEmpty() == true && id != null && id != 0) {
            val bearerToken = "Bearer $token"
//            profilViewModel.getProfile(bearerToken, id!!)
            dashboardViewModel.fetchSchedules(bearerToken, id!!)
            dashboardViewModel.fetchLiveStream(bearerToken, id!!)
        }
    }

//    if (profileState is GetProfileState.Loading || scheduleState is ScheduleState.Loading || liveState is LiveState.Loading) {
//        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            CircularProgressIndicator(color = Color(0xFF08607A))
//        }
//        return
//    }

//    val profileName = (profileState as? GetProfileState.Success)?.data?.name ?: "-"
    val isScheduleError = scheduleState is ScheduleState.Error
    val isLiveError = liveState is LiveState.Error

    if (isScheduleError || isLiveError) {
        val scheduleMsg = (scheduleState as? ScheduleState.Error)?.message ?: "Failed to fetch"
        val liveMsg = (liveState as? LiveState.Error)?.message ?: "Failed to fetch"

        Failed(
            navController = navController,
//            nama = profileName,
            scheduleError = scheduleMsg,
            liveError = liveMsg
        )
    } else {
        val schedules = (scheduleState as? ScheduleState.Success)?.data?.paginationData?.data ?: emptyList()
        val liveItems = (liveState as? LiveState.Success)?.data?.data ?: emptyList()

        if (schedules.isEmpty() && liveItems.isEmpty()) {
            Empty(
                navController = navController,
//                nama = profileName
            )
        } else {
            Success(
                navController = navController,
//                nama = profileName,
                schedules = schedules,
                liveItems = liveItems
            )
        }
    }
}

@Composable
fun Success(
    navController: NavController,
//    nama: String,
    schedules: List<ScheduleItem>,
    liveItems: List<LiveScheduleItem>
) {
    DashboardContent(
        navController = navController,
//        nama = nama,
        schedules = schedules,
        liveItems = liveItems,
        scheduleErrorMessage = null,
        liveErrorMessage = null
    )
}

@Composable
fun Failed(
    navController: NavController,
//    nama: String,
    scheduleError: String,
    liveError: String
) {
    DashboardContent(
        navController = navController,
//        nama = nama,
        schedules = emptyList(),
        liveItems = emptyList(),
        scheduleErrorMessage = scheduleError,
        liveErrorMessage = liveError
    )
}

@Composable
fun Empty(
    navController: NavController,
//    nama: String
) {
    DashboardContent(
        navController = navController,
//        nama = nama,
        schedules = emptyList(),
        liveItems = emptyList(),
        scheduleErrorMessage = null,
        liveErrorMessage = null
    )
}


@Composable
fun DashboardContent(
    navController: NavController,
//    nama: String,
    schedules: List<ScheduleItem>,
    liveItems: List<LiveScheduleItem>,
    scheduleErrorMessage: String?,
    liveErrorMessage: String?
) {
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
//                HeaderContent(nama)

                Spacer(modifier = Modifier.height(16.dp))
                MenuSection(navController)
                Spacer(modifier = Modifier.height(16.dp))
                ImageSlider()
                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Informasi Streaming Live Now",
                        color = Color(0xFF074A5D),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    when {
                        liveErrorMessage != null -> {
                            InformasiStreamingLive(judulSesi = liveErrorMessage, streamerId = 0, isError = true)
                        }
                        liveItems.isEmpty() -> {
                            LiveScheduleEmptyCard()
                        }
                        else -> {
                            for (liveItem in liveItems) {
                                InformasiStreamingLive(
                                    judulSesi = liveItem.title,
                                    streamerId = liveItem.userId
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Jadwal Minggu Ini",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF08607A),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    when {
                        scheduleErrorMessage != null -> {
                            ScheduleCard(title = scheduleErrorMessage, startTime = "Failed to fetch")
                        }
                        schedules.isEmpty() -> {
                            ScheduleEmptyCard()
                        }
                        else -> {
                            for (item in schedules) {
                                ScheduleCard(title = item.title, startTime = item.startTime)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleCard(title: String, startTime: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(text = "Mulai: $startTime", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun HeaderBackground() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF08607A), Color(0xFF84BBD1))))
    ) {
        Image(
            painter = painterResource(id = R.drawable.headertekstur),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun HeaderContent(nama: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(80.dp), verticalAlignment = Alignment.CenterVertically) {
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
                Text(text = "Hallo $nama", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = "Selamat Datang di Stream Management", fontSize = 14.sp, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Pantau jadwal & antrian mabar", fontSize = 12.sp, color = Color.White)
    }
}

@Composable
fun HelpDeskIcon(modifier: Modifier = Modifier, iconSize: Dp = 32.dp, onClick: () -> Unit, navController: NavController) {
    Row(
        modifier = modifier.fillMaxWidth().padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.SupportAgent,
            contentDescription = "Helpdesk",
            modifier = Modifier.size(iconSize).clickable { navController.navigate("Help-Desk") },
            tint = Color.White
        )
    }
}

@Composable
fun MenuSection(navController: NavController) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MenuItem(R.drawable.schedule, "Schedule", Modifier.weight(1f), Color(0xFFE3F2FD)) { navController.navigate("schedule") }
        MenuItem(R.drawable.portalberita, "Player", Modifier.weight(1f), Color(0xFFFFF9C4)) { navController.navigate("player") }
        MenuItem(R.drawable.ekms, "Donation", Modifier.weight(1f), Color(0xFFFFE4E9)) { navController.navigate("donation ") }
    }
}

@Composable
fun MenuItem(drawableResId: Int, label: String, modifier: Modifier = Modifier, backgroundColor: Color = Color(0xFFF0F0F0), onClick: () -> Unit = {}) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = drawableResId), contentDescription = null, modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ScheduleEmptyCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF8F9FA))
            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Belum ada jadwal streaming mabar untuk minggu ini.", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun LiveScheduleEmptyCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF8F9FA))
            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Tidak ada live streaming berjalan saat ini.", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ImageSlider(imageList: List<Int> = listOf(R.drawable.slider1, R.drawable.slider2, R.drawable.slider3, R.drawable.slider4), slideDurationMillis: Long = 3000L) {
    val pagerState = rememberPagerState()
    LaunchedEffect(Unit) {
        while (true) {
            delay(slideDurationMillis)
            val nextPage = (pagerState.currentPage + 1) % imageList.size
            pagerState.animateScrollToPage(nextPage)
        }
    }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(count = imageList.size, state = pagerState, modifier = Modifier.fillMaxWidth().height(200.dp)) { page ->
            Image(
                painter = painterResource(id = imageList[page]),
                contentDescription = "Image $page",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
fun InformasiStreamingLive(judulSesi: String = "-", streamerId: Int = 0, isError: Boolean = false) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isError) Brush.horizontalGradient(colors = listOf(Color(0xFFD32F2F), Color(0xFFEF5350)))
                        else Brush.horizontalGradient(colors = listOf(Color(0xFF08607A), Color(0xFF84BBD1)))
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = judulSesi, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White, modifier = Modifier.weight(1f))
                    if (!isError) {
                        Box(modifier = Modifier.background(Color.Red, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("LIVE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(imageVector = Icons.Filled.Person, contentDescription = "Name", tint = Color(0xFF08607A), modifier = Modifier.size(20.dp))
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Streamer", fontSize = 14.sp, modifier = Modifier.width(80.dp))
//                    Text(text = ": $streamerName", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                }
//                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "ID", tint = Color(0xFF08607A), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Host ID", fontSize = 14.sp, modifier = Modifier.width(80.dp))
                    Text(text = if (isError) ": -" else ": $streamerId", fontSize = 14.sp)
                }
            }
        }
    }
}

// ====================================================
// PREVIEWS
// ====================================================

@Preview(name = "Dashboard - Success State", showBackground = true, showSystemUi = true)
@Composable
fun DashboardSuccessPreview() {
    val fakeNavController = androidx.navigation.compose.rememberNavController()

    val dummyLiveItems = listOf(
        LiveScheduleItem(id = 3, userId = 99281, title = "Mabar Push Mythic Immo!", startTime = "20:00", status = "live")
    )

    val dummySchedules = listOf(
        ScheduleItem(id = 1, userId = 12, title = "Mabar Subsciber Day", startTime = "19:00", status = "scheduled"),
        ScheduleItem(id = 2, userId = 12, title = "Push Rank Immo", startTime = "22:00", status = "scheduled")
    )

    Success(
        navController = fakeNavController,
//        nama = "Lidia Sola",
        schedules = dummySchedules,
        liveItems = dummyLiveItems
    )
}

@Preview(name = "Dashboard - Failed State", showBackground = true, showSystemUi = true)
@Composable
fun DashboardFailedPreview() {
    val fakeNavController = androidx.navigation.compose.rememberNavController()
    Failed(
        navController = fakeNavController,
//        nama = "Lidia Sola",
        scheduleError = "Failed to fetch schedules",
        liveError = "Failed to fetch live stream"
    )
}

@Preview(name="Dashboard - Empty State", showBackground = true, showSystemUi = true)
@Composable
fun DashboardEmptyPreview(){
    val fakeNavController = androidx.navigation.compose.rememberNavController()
    Empty(
        navController = fakeNavController,
//        nama = "Lidia Sola"
    )
}