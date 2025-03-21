package com.example.expirycheck.customs

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NoItemsText(text: String) {
    Text(text, style = MaterialTheme.typography.headlineLarge)
}