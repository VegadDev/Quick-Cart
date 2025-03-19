package com.dev.quickcart.screens.cart

import com.dev.quickcart.data.model.CartItem

data class CartUiState(

    val isLoading: Boolean = false,
    val cartItems: List<CartItem> = emptyList(),
    val error: String? = null,

    var cartCount: Int = 0,

)
