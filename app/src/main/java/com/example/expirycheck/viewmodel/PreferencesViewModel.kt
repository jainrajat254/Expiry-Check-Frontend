package com.example.expirycheck.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expirycheck.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    init {
        viewModelScope.launch {
            preferencesRepository.isDarkMode().collect { value ->
                _isDarkMode.value = value
            }
        }
    }

    fun enableDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.saveDarkMode(enabled)
        }
    }
}
