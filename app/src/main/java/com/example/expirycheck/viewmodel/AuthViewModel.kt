package com.example.expirycheck.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.expirycheck.models.LoginRequest
import com.example.expirycheck.models.LoginResponse
import com.example.expirycheck.models.RegisterRequest
import com.example.expirycheck.navigation.Routes
import com.example.expirycheck.repository.AuthRepository
import com.example.expirycheck.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPreferencesManager: PreferencesRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var authError by mutableStateOf<String?>(null)
        private set

    var loginResponse by mutableStateOf<LoginResponse?>(null)
        private set

    fun login(
        loginRequest: LoginRequest,
        onSuccess: (LoginResponse) -> Unit = {},
    ) {
        isLoading = true
        authError = null

        viewModelScope.launch {
            try {
                val response = authRepository.login(loginRequest)
                if (response.isSuccessful && response.body() != null) {
                    loginResponse = response.body()
                    onSuccess(loginResponse!!)
                } else {
                    authError = "Login failed: ${response.message()}"
                }
            } catch (e: HttpException) {
                Log.e("AuthViewModel", "HttpException: ${e.message}")
                authError = "HTTP error during login: ${e.message}"
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Exception: ${e.message}")
                authError = "Unexpected error during login: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun register(
        registerRequest: RegisterRequest,
        onSuccess: (LoginResponse) -> Unit = {},
    ) {
        isLoading = true
        authError = null

        viewModelScope.launch {
            try {
                val response = authRepository.register(registerRequest)
                if (response.isSuccessful && response.body() != null) {
                    loginResponse = response.body()
                    onSuccess(loginResponse!!)
                } else {
                    authError = "Registration failed: ${response.message()}"
                }
            } catch (e: HttpException) {
                Log.e("AuthViewModel", "HttpException: ${e.message}")
                authError = "HTTP error during register: ${e.message}"
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Exception: ${e.message}")
                authError = "Unexpected error during register: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun logout(navController: NavController) {
        viewModelScope.launch {
            sharedPreferencesManager.clearUserData()
        }
        navController.navigate(Routes.Login.routes) {
            popUpTo(Routes.Home.routes) { inclusive = true }
        }
    }

    fun clearState() {
        isLoading = false
        authError = null
        loginResponse = null
    }
}

