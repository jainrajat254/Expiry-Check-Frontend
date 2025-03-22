package com.example.expirycheck.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expirycheck.customs.TopAppBar
import com.example.expirycheck.navigation.BottomAppBar
import com.example.expirycheck.navigation.Routes

@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar("Settings") },
        bottomBar = { BottomAppBar(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                SettingsItem(title = "Notification Settings") {
                    navController.navigate(Routes.Notifications.routes)
                }
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                SettingsItem(title = "Theme Settings") {
                    navController.navigate(Routes.Theme.routes)
                }
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                SettingsItem(title = "Password Settings") {
                    navController.navigate(Routes.Password.routes)
                }
            }
        }
    }
}

@Composable
fun SettingsItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp)
        )
    }
}
