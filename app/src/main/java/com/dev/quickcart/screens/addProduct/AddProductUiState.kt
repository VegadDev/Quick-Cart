package com.dev.quickcart.screens.addProduct

import com.dev.quickcart.data.model.Product

data class AddProductUiState(

    val product: Product? = null,

    val productName: String = "",
    val productNameError: String = ""
)
