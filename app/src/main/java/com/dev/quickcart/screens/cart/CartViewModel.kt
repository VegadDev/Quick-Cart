package com.dev.quickcart.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.data.repository.NetworkRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel
@Inject
constructor(
    private val auth: FirebaseAuth,
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    val interActor = object : CartInterActor {


    }
    init {
        fetchCartItems()
    }

    fun fetchCartItems() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                _uiState.value = CartUiState(error = "User not signed in")
                uiState.value.cartCount = 0
                return@launch
            }

            _uiState.value = CartUiState(isLoading = true)
            networkRepository.getCartItems(userId).collect { result ->
                if (result.isSuccess) {
                    val cartItems = result.getOrNull() ?: emptyList()
                    val totalCount = cartItems.sumOf { it.quantity } // Calculate total count
                    _uiState.value = CartUiState(cartItems = cartItems)
                    uiState.value.cartCount = totalCount
                } else {
                    _uiState.value = CartUiState(error = result.exceptionOrNull()?.message)
                    uiState.value.cartCount = 0
                }
            }
        }
    }

    // Optional: Function to update quantity (if used in Cart Items Screen)
    fun updateQuantity(productId: String, newQuantity: Int) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val result = networkRepository.updateCartItemQuantity(userId, productId, newQuantity)
            if (result.isSuccess) {
                // No need to call fetchCartItems here; Flow will update automatically
            } else {
                _uiState.value = CartUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

}