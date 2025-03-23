package com.example.expirycheck.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.expirycheck.R

@Composable
fun ThemeChangeSwitch(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = darkTheme,
            onCheckedChange = onDarkThemeChange,
            thumbContent = {
                Image(
                    painter = painterResource(id = if (darkTheme) R.drawable.baseline_dark_mode_24 else R.drawable.baseline_light_mode_24),
                    contentDescription = "Theme Icon",
                    modifier = Modifier.size(16.dp),
                    colorFilter = ColorFilter.tint(if (darkTheme) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                )
            },
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSecondary,
            )
        )
    }
}
