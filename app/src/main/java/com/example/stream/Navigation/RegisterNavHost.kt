package com.example.stream.Navigation//package com.example.stream.Navigation
//
//import androidx.activity.ComponentActivity
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.stream.ui.Screen.Register.CompleteDataScreen
//import com.example.stream.ui.Screen.Register.PasswordScreen
//import com.example.stream.ui.Screen.Register.RegisterScreen
//import com.example.stream.ui.Screen.Register.RegisterViewModel
//
//@Composable
//fun RegisterNavHost(navController: NavHostController = rememberNavController()){
//    val viewModel: RegisterViewModel = viewModel(LocalContext.current as ComponentActivity)
//
//    NavHost(navController, startDestination = "email") {
//        composable("email") {
//            RegisterScreen(navController, viewModel)
//        }
//        composable("password") {
//            PasswordScreen(navController, viewModel)
//        }
//        composable("complete_data") {
//            CompleteDataScreen(navController, viewModel, onSuccessRegister = {})
//        }
//    }
//}