package com.example.expirycheck.navigation

sealed class Routes(var routes: String) {

    data object Login: Routes("login")
    data object Register: Routes("register")
    data object Home: Routes("home")
}