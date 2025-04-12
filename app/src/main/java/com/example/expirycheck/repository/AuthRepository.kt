package com.example.expirycheck.repository

import com.example.expirycheck.models.LoginRequest
import com.example.expirycheck.models.LoginResponse
import com.example.expirycheck.models.RegisterRequest
import com.example.expirycheck.retrofit.UserService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @Named("BackendService") private val userService: UserService
) {

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return userService.login(loginRequest)
    }

    suspend fun register(registerRequest: RegisterRequest): Response<LoginResponse> {
        return userService.register(registerRequest)
    }
}
