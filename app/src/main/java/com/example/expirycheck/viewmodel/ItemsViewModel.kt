package com.example.expirycheck.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expirycheck.models.AddItems
import com.example.expirycheck.models.Items
import com.example.expirycheck.repository.ItemsRepository
import com.example.expirycheck.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemRepository: ItemsRepository,
    private val sharedPreferences: PreferencesRepository
) : ViewModel() {

    // State management using mutableStateList for automatic UI updates
    private val _items = mutableStateListOf<Items>()
    val items: List<Items> get() = _items

    // Loading state
    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    // Error state
    private val _error = mutableStateOf<String?>(null)
    val error: String? get() = _error.value

    // Initial load
    init {
        getItems()
    }

    fun addItems(addItems: AddItems) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = itemRepository.addItems(addItems)
                if (response.isSuccessful && response.body() != null) {
                    _items.add(response.body()!!)
                } else {
                    _error.value = "Failed to add items: ${response.message()}"
                }
            } catch (e: HttpException) {
                _error.value = "HTTP Error: ${e.message()}"
                Log.e("ViewModel", "Add items HTTP error", e)
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("ViewModel", "Add items error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getItems(username: String = "defaultUser") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = itemRepository.getItems(username)
                val body = response.body()
                if (body != null) {
                    _items.clear()
                    _items.addAll(body)
                    sharedPreferences.saveItemsList(_items)
                } else {
                    _error.value = "Failed to get items: ${response.message()}"
                }
            } catch (e: HttpException) {
                _error.value = "HTTP Error: ${e.message()}"
                Log.e("ViewModel", "Get items HTTP error", e)
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("ViewModel", "Get items error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeItem(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = itemRepository.removeItem(id)
                if (response.isSuccessful) {
                    _items.removeAll { it.id == id }
                    sharedPreferences.removeItemFromList(id)
                } else {
                    _error.value = "Failed to remove item: ${response.message()}"
                    getItems()
                }
            } catch (e: HttpException) {
                _error.value = "HTTP Error: ${e.message()}"
                Log.e("ViewModel", "Remove item HTTP error", e)
                getItems() // Refresh on error
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("ViewModel", "Remove item error", e)
                getItems() // Refresh on error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}