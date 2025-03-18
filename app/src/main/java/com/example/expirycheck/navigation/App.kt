package com.example.expirycheck.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expirycheck.screens.HomeScreen
import com.example.expirycheck.screens.LoginScreen
import com.example.expirycheck.screens.RegisterScreen
import com.example.expirycheck.screens.SplashScreen
import com.example.expirycheck.viewmodel.UserViewModel

@Composable
fun App() {

    val navController = rememberNavController()
    val vm = hiltViewModel<UserViewModel>()
    val modifier = Modifier

    NavHost(navController = navController, startDestination = Routes.Splash.routes) {

        composable(Routes.Splash.routes) {
            SplashScreen(navController = navController, vm = vm, modifier = modifier)
        }
        composable(Routes.Login.routes) {
            LoginScreen()
        }
        composable(Routes.Register.routes) {
            RegisterScreen()
        }
        composable(Routes.Home.routes) {
            HomeScreen()
        }

    }
}