package com.example.expirycheck.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expirycheck.customs.TopAppBar
import com.example.expirycheck.repository.PreferencesRepository
import com.example.expirycheck.viewmodel.PreferencesViewModel

@Composable
fun ThemeSettings() {

    val context = LocalContext.current
    val prefsViewModel: PreferencesViewModel = hiltViewModel()
    val darkThemeState = prefsViewModel.getThemeMode(context).collectAsState().value

    Scaffold(
        topBar = { TopAppBar("Change Theme") }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                PreferencesRepository.ThemeMode.entries.forEach { themeMode ->
                    Spacer(modifier = Modifier.fillMaxHeight())

                    if (themeMode.ordinal == darkThemeState) {
                        Button(
                            onClick = { /* No action needed, theme is already selected */ }
                        ) {
                            Text(text = themeMode.name)
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                prefsViewModel.setThemeMode(context, themeMode) // Update theme
                            }
                        ) {
                            Text(text = themeMode.name)
                        }
                    }
                    Spacer(modifier = Modifier.fillMaxHeight())
                }
            }
        }
    }
}
