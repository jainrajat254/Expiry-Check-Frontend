package com.example.expirycheck.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expirycheck.viewmodel.UserViewModel

@Composable
fun SplashScreen(
    navController: NavController = rememberNavController(),
    vm: UserViewModel = viewModel(),
    modifier: Modifier = Modifier,
) {
    Text(text = "Hello")
}

@Composable
@Preview(showBackground = true)
fun SplashScreenPreview() {
    SplashScreen()
}