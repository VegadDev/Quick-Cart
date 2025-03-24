package com.dev.quickcart.screens.productPage

import com.dev.quickcart.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProductPageUiState(


    val products: Product? = null,
    val isLoading: Boolean = true,
    val error: String? = null,

    val productName: String = "",
    val productImage: String = "",
    val productPrice: String = "",
    val prodDescription: String = "",
    val productTypeValue: String = "",
    val prodCalories: String = "",
    val prodProtein: String = "",
    val prodFat: String = "",
    val prodFiber: String = "",







)
