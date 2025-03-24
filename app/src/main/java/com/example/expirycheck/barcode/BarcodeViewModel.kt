package com.example.expirycheck.barcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarcodeViewModel @Inject constructor(
    private val barcodeRepository: BarcodeRepository
) : ViewModel() {

    private val _barcodeResult = MutableStateFlow<BarcodeResponse?>(null)
    val barcodeResult = _barcodeResult.asStateFlow()

    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            val result = barcodeRepository.getProduct(barcode)
            _barcodeResult.value = result
        }
    }
}
