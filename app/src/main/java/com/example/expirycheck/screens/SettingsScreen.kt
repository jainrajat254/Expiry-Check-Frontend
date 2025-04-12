package com.example.expirycheck.screens

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.expirycheck.customs.CustomButton
import com.example.expirycheck.customs.SettingsCard
import com.example.expirycheck.customs.ThemeSelectionRow
import com.example.expirycheck.customs.TopAppBar
import com.example.expirycheck.navigation.BottomAppBar
import com.example.expirycheck.navigation.Routes
import com.example.expirycheck.viewmodel.AuthViewModel
import com.example.expirycheck.viewmodel.PreferencesViewModel
import java.util.Locale

@Composable
fun SettingsScreen(
    navController: NavController,
    pvm: PreferencesViewModel,
    vm: AuthViewModel,
) {
    val context = LocalContext.current
    val hour by pvm.hour.collectAsState()
    val minute by pvm.minute.collectAsState()
    var showTimePicker by remember { mutableStateOf(false) }
    val selectedTheme by pvm.selectedTheme.collectAsState()

    val notificationPermissionGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    Scaffold(
        topBar = { TopAppBar("Settings") },
        bottomBar = { BottomAppBar(navController) }
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
                SettingsCard(
                    title = "Notification time",
                    description = """
                        Each day you will receive a notification with a list of:
                        - already expired
                        - that expire on the current date
                        - that have one day left until expiration.
                    """.trimIndent()
                ) {
                    Text(
                        text = String.format(Locale.US, "%02d:%02d", hour, minute),
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (notificationPermissionGranted)
                            MaterialTheme.colorScheme.onSurface
                        else
                            MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .clickable {
                                if (notificationPermissionGranted) {
                                    showTimePicker = true
                                }
                            }
                    )
                    if (!notificationPermissionGranted) {
                        Text(
                            text = "Notification Permissions are not granted. Please enable it in settings.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            if (showTimePicker) {
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        // Log the selected time for debugging
                        Log.d("Notification", "Selected time: $hour:$minute")

                        pvm.saveNotificationTime(context, hour, minute)
                        showTimePicker = false
                    },
                    hour,
                    minute,
                    false
                ).show()
            }

            item {
                SettingsCard(title = "Theme") {
                    ThemeSelectionRow(selectedTheme) { themeMode -> pvm.saveThemeMode(themeMode) }
                }
            }
            item {
                SettingsCard(title = "Password") {
                    Text("Change Password", modifier = Modifier.clickable {
                        navController.navigate(Routes.Password.routes)
                    })
                    Text("Forgot Password", modifier = Modifier
                        .padding(top = 12.dp)
                        .clickable {
                            navController.navigate(Routes.Password.routes)
                        })
                }
            }
            item {
                CustomButton(
                    text = "Logout",
                    onClick = {
                        vm.logout(navController = navController)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout Icon",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                )

            }
        }
    }
}
