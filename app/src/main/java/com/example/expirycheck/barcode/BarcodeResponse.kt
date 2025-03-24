package com.example.expirycheck.barcode

import com.google.gson.annotations.SerializedName

data class BarcodeResponse (
    @SerializedName("code")
    var code: String,
    @SerializedName("product")
    var product: Product,
    @SerializedName("status")
    var status: Int
)

data class Product(
    @SerializedName("brands")
    var brands: String,
    @SerializedName("product_name")
    var productName: String,
    @SerializedName("image_thumb_url")
    var imageThumbUrl: String
)