package com.example.stream.Navigation//package com.example.stream.Navigation
//
//import androidx.activity.ComponentActivity
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.navArgument
//import com.example.stream.Data.Model.Request.PosisiKeluarga
//import com.example.stream.ui.Screen.AnggotaKeluarga.AnggotaKeluargaScreen
//import com.example.stream.ui.Screen.AnggotaKeluarga.AnggotaKeluargaViewModel
//import com.example.stream.ui.Screen.AnggotaKeluarga.CompleteAnggotaKeluargaScreen
//
//@Composable
//fun AnggotaNavHost(navController: NavHostController = rememberNavController()){
//    val viewModel: AnggotaKeluargaViewModel = viewModel(LocalContext.current as ComponentActivity)
//
//    NavHost(navController, startDestination = "anggota") {
//        composable(
//            "anggota/{no_kk}",
//            arguments = listOf(navArgument("no_kk") { type = NavType.StringType })
//        ) {
//            backStackEntry ->
//            val noKk = backStackEntry.arguments?.getString("no_kk") ?: ""
//
//            viewModel.saveNoKK(noKk)
//
//            AnggotaKeluargaScreen(
//                navController = navController,
//                viewModel = viewModel,
//            )
//        }
//        composable(
//            route = "Completed-Anggota/{posisi}",
//            arguments = listOf(
//                navArgument("posisi") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//            val posisiStr = backStackEntry.arguments?.getString("posisi") ?: ""
//
//            val posisiKeluarga = try {
//                PosisiKeluarga.valueOf(posisiStr)
//            } catch (e: Exception) {
//                null
//            }
//
//            if (posisiKeluarga != null) {
//                viewModel.savePosisiKeluarga(posisiKeluarga)
//            }
//
//            CompleteAnggotaKeluargaScreen(
//                navController = navController,
//                viewModel = viewModel,
//                onSuccessAnggotaKeluargaState = {}
//            )
//        }
//    }
//}