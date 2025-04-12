package com.example.expirycheck.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expirycheck.barcode.BarcodeViewModel
import com.example.expirycheck.repository.PreferencesRepository
import com.example.expirycheck.screens.AddItemsScreen
import com.example.expirycheck.screens.HomeScreen
import com.example.expirycheck.screens.ItemListScreen
import com.example.expirycheck.screens.LoginScreen
import com.example.expirycheck.screens.PasswordSettings
import com.example.expirycheck.screens.RegisterScreen
import com.example.expirycheck.screens.SettingsScreen
import com.example.expirycheck.viewmodel.PreferencesViewModel
import com.example.expirycheck.viewmodel.AuthViewModel
import com.example.expirycheck.viewmodel.ItemsViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun App() {
    val navController = rememberNavController()
    val vm = hiltViewModel<AuthViewModel>()
    val ivm = hiltViewModel<ItemsViewModel>()
    val pvm: PreferencesViewModel = hiltViewModel()
    val context = LocalContext.current
    val sharedPreferencesManager = PreferencesRepository(context = context)
    val bvm: BarcodeViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Routes.Login.routes) {
        composable(Routes.Login.routes) {
            LoginScreen(
                navController = navController,
                vm = vm,
                sharedPreferencesManager = sharedPreferencesManager
            )
        }
        composable(Routes.Register.routes) {
            RegisterScreen(
                navController = navController,
                vm = vm,
                sharedPreferencesManager = sharedPreferencesManager
            )
        }
        composable(Routes.Home.routes) {
            HomeScreen(navController = navController,ivm = ivm, sharedPreferencesManager = sharedPreferencesManager)
        }
        composable(Routes.List.routes) {
            ItemListScreen(navController = navController, ivm = ivm,sharedPreferencesManager = sharedPreferencesManager)
        }
        composable(Routes.AddItems.routes) {
            AddItemsScreen(navController = navController, ivm = ivm, bvm = bvm, sharedPreferencesManager = sharedPreferencesManager)
        }
        composable(Routes.Settings.routes) {
            SettingsScreen(navController = navController, pvm = pvm, vm = vm)
        }
        composable(Routes.Password.routes) {
            PasswordSettings()
        }

    }
}

