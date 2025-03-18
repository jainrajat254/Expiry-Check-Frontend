package com.example.expirycheck.repository

import com.example.expirycheck.retrofit.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService
) {
}