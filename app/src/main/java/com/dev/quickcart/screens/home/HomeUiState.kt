package com.dev.quickcart.screens.home

import com.dev.quickcart.data.model.CartItem
import com.dev.quickcart.data.model.Product
import com.dev.quickcart.data.model.UserAddress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class HomeUiState(


    val productList: List<Product> = emptyList(),
    var isLoading: Boolean = false,
    var isLoadingOnATC: Boolean = false,
    val error: String? = null,


    val searchInput: String = "",
    val searchInputError: String = "",

    val userName: String = "",
    val userImage: String? = null,

    var cartCount: Int = 0,

    val isAdding: Boolean = false, // New field for button loading state
    val cartItems: List<CartItem> = emptyList(),
    var addresses: List<UserAddress> = emptyList(),


    val loadingState: StateFlow<Map<String, Boolean>> = MutableStateFlow(emptyMap()),

    )



