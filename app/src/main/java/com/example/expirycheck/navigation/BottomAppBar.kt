package com.example.expirycheck.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomAppBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(
            Routes.Home.routes,
            Icons.Filled.Home,
            Icons.Outlined.Home,
            "Home"
        ),
        BottomNavItem(
            Routes.List.routes,
            Icons.AutoMirrored.Filled.List,
            Icons.AutoMirrored.Outlined.List,
            "Profile"
        ),
        BottomNavItem(
            Routes.Settings.routes,
            Icons.Filled.Settings,
            Icons.Outlined.Settings,
            "Settings"
        )
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(Routes.Home.routes) { inclusive = false }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unSelectedIcon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

