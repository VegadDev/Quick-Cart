package com.dev.quickcart.screens.home

import com.dev.quickcart.data.model.Product

interface HomeInterActor {

    fun updateSearchInput(it: String)
    fun gotoProductPage(it: Int)
    fun gotoCart(string: String)
    suspend fun addToCart(product: Product)
    fun gotoProfile()
    fun gotoAddAddress()
    fun selectAddressCategory(category: String)

    fun setSelectedAddress(address: String)

}


