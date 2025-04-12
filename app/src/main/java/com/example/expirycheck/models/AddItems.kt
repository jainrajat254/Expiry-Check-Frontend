package com.example.expirycheck.models

data class  AddItems(
    val itemName: String,
    val expiryDate: String,
    val openingDate: String? = null,
    val timeSpan: String? = null,
    val username: String
)
