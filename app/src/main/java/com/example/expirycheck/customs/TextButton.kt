package com.example.expirycheck.customs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CustomTextButton(
    normalText: String = "",
    clickableText: String,
    onClick: () -> Unit,
    normalTextColor: Color = MaterialTheme.colorScheme.onSurface,
    clickableTextColor: Color = MaterialTheme.colorScheme.primary,
) {
    Row {
        if (normalText.isNotEmpty()) {
            Text(text = normalText, color = normalTextColor)
        }
        Text(
            text = clickableText,
            color = clickableTextColor,
            modifier = Modifier.clickable { onClick() }
        )
    }
}
