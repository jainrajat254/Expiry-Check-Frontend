package com.example.expirycheck.models

import com.google.gson.annotations.SerializedName

data class Items(
    @SerializedName("id")
    val id: String,
    val itemName: String,
    val expiryDate: String,
    val openingDate: String,
    val timeSpan: String,
)
