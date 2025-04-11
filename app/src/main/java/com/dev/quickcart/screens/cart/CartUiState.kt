package com.dev.quickcart.screens.cart

import com.dev.quickcart.data.model.CartItem
import com.dev.quickcart.data.model.UserAddress

data class CartUiState(

    val isLoading: Boolean = false,
    val cartItems: List<CartItem> = emptyList(),
    val error: String? = null,
    var loadingItemsMinus: Map<String, Boolean> = emptyMap(),
    var loadingItemsPlus: Map<String, Boolean> = emptyMap(),
    val loadingItems: Map<String, Boolean> = emptyMap(),
    var cartCount: Int = 0,
    var cartTotal: Int = 0,
    val totalPrice: Double = 0.0,

    val selectedAddress: UserAddress? = null,

    )
