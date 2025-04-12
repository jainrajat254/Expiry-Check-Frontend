package com.example.expirycheck.retrofit

import com.example.expirycheck.barcode.BarcodeResponse
import com.example.expirycheck.models.AddItems
import com.example.expirycheck.models.Items
import com.example.expirycheck.models.LoginRequest
import com.example.expirycheck.models.LoginResponse
import com.example.expirycheck.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @GET("{barcode}?fields=brands,product_name,code,image_thumb_url")
    suspend fun getProduct(@Path("barcode") barcode: String): Response<BarcodeResponse>

    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<LoginResponse>

    @POST("/items/add-items")
    suspend fun addItems(@Body addItems: AddItems): Response<Items>

    @GET("/items/get-items/{username}")
    suspend fun getItems(@Path("username") username: String): Response<List<Items>>

    @DELETE("/items/remove-item/{id}")
    suspend fun removeItem(@Path("id") id: String):  Response<List<Items>>

}