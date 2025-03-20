package com.example.expirycheck.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val label: String,
)
