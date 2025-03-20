package com.example.expirycheck.screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.expirycheck.navigation.BottomAppBar
import com.example.expirycheck.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    vm: UserViewModel,
) {

    val activity = LocalActivity.current as ComponentActivity

    BackHandler {
        activity.finish()
    }

    Scaffold(
        bottomBar = { BottomAppBar(navController = navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text(text = "Hello")
            }
        }
    )
}

