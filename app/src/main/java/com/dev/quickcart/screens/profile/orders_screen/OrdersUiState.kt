package com.dev.quickcart.screens.profile.orders_screen

import com.dev.quickcart.data.model.Order

data class OrdersUiState(

    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val error: String? = null
)
