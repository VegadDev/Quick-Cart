package com.dev.quickcart.screens.home

import com.dev.quickcart.data.model.Product

data class HomeUiState(


    val productList: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,


    val searchInput: String = "",
    val searchInputError: String = "",

    val userName: String = "",
    val userImage: String? = null,


)



