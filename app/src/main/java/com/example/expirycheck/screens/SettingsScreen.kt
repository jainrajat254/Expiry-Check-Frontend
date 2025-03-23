package com.example.expirycheck.screens

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expirycheck.customs.SettingsCard
import com.example.expirycheck.customs.ThemeSelectionRow
import com.example.expirycheck.customs.TopAppBar
import com.example.expirycheck.navigation.BottomAppBar
import com.example.expirycheck.navigation.Routes
import com.example.expirycheck.viewmodel.PreferencesViewModel
import java.util.Locale

@Composable
fun SettingsScreen(navController: NavController, pvm: PreferencesViewModel) {
    val context = LocalContext.current
    val hour by pvm.hour.collectAsState()
    val minute by pvm.minute.collectAsState()
    var showTimePicker by remember { mutableStateOf(false) }
    val selectedTheme by pvm.selectedTheme.collectAsState()

    Scaffold(
        topBar = { TopAppBar("Settings") },
        bottomBar = { BottomAppBar(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .alpha(1.5f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                SettingsCard(title = "Notification time", description =
                "Each day you will receive a notification with a list of:\n" +
                        "- already expired\n- that expire on the current date\n" +
                        "- that have one day left until expiration."
                ) {
                    Text(
                        text = String.format(Locale.US, "%02d:%02d", hour, minute),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .clickable { showTimePicker = true }
                    )

                }

                if (showTimePicker) {
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            pvm.saveNotificationTime(context, hour, minute)
                            showTimePicker = false
                        },
                        hour,
                        minute,
                        false
                    ).show()
                }
            }
            item {
                SettingsCard(title = "Theme") {
                    ThemeSelectionRow(selectedTheme) { themeMode -> pvm.saveThemeMode(themeMode) }
                }
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                TextButton(modifier = Modifier.padding(vertical = 8.dp), onClick = { navController.navigate(Routes.Password.routes) }) {
                    Text("Manage Password")
                }
            }
        }
    }
}



