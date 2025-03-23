package com.example.expirycheck.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expirycheck.customs.TopAppBar
import com.example.expirycheck.navigation.BottomAppBar
import com.example.expirycheck.navigation.Routes

@Composable
fun SettingsScreen(
    navController: NavController,
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit
) {
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

            // ✅ Theme Settings Item with Switch
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Change Theme",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    ThemeChangeSwitch(darkTheme = darkTheme, onDarkThemeChange = onDarkThemeChange)
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
