package com.example.expirycheck.navigation

sealed class Routes(var routes: String) {

    data object Splash: Routes("splash")
    data object Login: Routes("login")
    data object Register: Routes("register")
    data object Home: Routes("home")
}