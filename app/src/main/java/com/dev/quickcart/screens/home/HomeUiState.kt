package com.dev.quickcart.screens.home

import com.dev.quickcart.data.model.Product

data class HomeUiState(



    val productList: List<Product> = emptyList(),


    val searchInput: String = "",
    val searchInputError: String = "",

    val userName: String = "",
    val userImage: String? = null,


    val prodName: String = "",
    val prodImage: String = "",
    val prodPrice: String = "",
    val prodDescription: String = "",


)



