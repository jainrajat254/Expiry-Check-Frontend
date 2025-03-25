package com.example.expirycheck.barcode

import com.example.expirycheck.retrofit.UserService
import javax.inject.Inject

class BarcodeRepository @Inject constructor(
    private val userService: UserService,
) {
    suspend fun getProduct(barcode: String): BarcodeResponse? {
        return try {
            val response = userService.getProduct(barcode)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
