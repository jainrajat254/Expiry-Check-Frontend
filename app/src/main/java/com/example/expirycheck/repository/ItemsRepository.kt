package com.example.expirycheck.repository

import com.example.expirycheck.models.AddItems
import com.example.expirycheck.models.Items
import com.example.expirycheck.retrofit.UserService
import jakarta.inject.Inject
import retrofit2.Response
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ItemsRepository @Inject constructor(
    @Named("BackendService") private val userService: UserService,
) {

    suspend fun addItems(addItems: AddItems): Response<Items> {
        return userService.addItems(addItems)
    }

    suspend fun getItems(username: String): Response<List<Items>> {
        return userService.getItems(username)
    }

    suspend fun removeItem(id: String): Response<List<Items>> {
        return userService.removeItem(id)
    }
}