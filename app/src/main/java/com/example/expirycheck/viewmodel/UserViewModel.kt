package com.example.expirycheck.viewmodel

import androidx.lifecycle.ViewModel
import com.example.expirycheck.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

}