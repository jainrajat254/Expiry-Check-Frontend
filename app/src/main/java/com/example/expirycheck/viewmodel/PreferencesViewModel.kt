package com.example.expirycheck.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expirycheck.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PreferencesViewModel @Inject constructor() : ViewModel() {

    private val _themeMode = MutableStateFlow(PreferencesRepository.ThemeMode.SYSTEM.ordinal)
    val themeMode: StateFlow<Int> = _themeMode.asStateFlow()

    fun getThemeMode(context: Context): StateFlow<Int> {
        viewModelScope.launch {
            _themeMode.value = PreferencesRepository.getThemeMode(context)
        }
        return themeMode
    }

    fun setThemeMode(context: Context, theme: PreferencesRepository.ThemeMode) {
        viewModelScope.launch {
            PreferencesRepository.setThemeMode(
                context = context,
                themeMode = theme
            )
        }
        _themeMode.value = theme.ordinal
    }

}

