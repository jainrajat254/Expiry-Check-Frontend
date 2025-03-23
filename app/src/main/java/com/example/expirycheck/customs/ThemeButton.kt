package com.example.expirycheck.customs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class ThemeMode { LIGHT, DARK, SYSTEM }

@Composable
fun ThemeSelectionRow(selectedTheme: ThemeMode, onThemeSelected: (ThemeMode) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        listOf(ThemeMode.LIGHT, ThemeMode.SYSTEM, ThemeMode.DARK).forEach { themeMode ->
            ThemeButton(
                themeMode.name.lowercase().replaceFirstChar { it.uppercase() },
                selectedTheme == themeMode
            ) {
                onThemeSelected(themeMode)
            }
        }
    }
}

@Composable
fun ThemeButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(text)
    }
}
