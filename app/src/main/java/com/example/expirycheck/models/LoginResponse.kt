package com.example.expirycheck.models

data class LoginResponse(
    val fullName: String,
    val username: String,
    val items: List<Items>,
)
