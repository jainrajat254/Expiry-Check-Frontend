package com.example.expirycheck.barcode

import androidx.datastore.core.IOException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BarcodeViewModel @Inject constructor(
    private val barcodeRepository: BarcodeRepository,
) : ViewModel() {

    private val _barcodeResult = MutableStateFlow<BarcodeResponse?>(null)
    val barcodeResult = _barcodeResult.asStateFlow()

    fun fetchProduct(barcode: String, onFailure: (String) -> Unit) {
        if (barcode.isBlank() || barcode.length !in 8..14) {  // Typical barcode lengths
            onFailure("Invalid barcode. Please scan again.")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = barcodeRepository.getProduct(barcode)

                withContext(Dispatchers.Main) {
                    when {
                        result == null -> {
                            onFailure("No response from server. Try again.")
                        }

                        result.status == 0 || result.product.productName.isBlank() -> {
                            onFailure("Product not found. Check the barcode.")
                            _barcodeResult.value = null
                        }

                        else -> {
                            _barcodeResult.value = result
                        }
                    }
                }
            } catch (e: TimeoutCancellationException) {
                withContext(Dispatchers.Main) {
                    onFailure("Request timed out. Check your internet connection.")
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    onFailure("Network error. Please try again.")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure("Error fetching product: ${e.message}")
                }
            }
        }
    }

}
