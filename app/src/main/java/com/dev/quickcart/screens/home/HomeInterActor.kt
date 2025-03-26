package com.dev.quickcart.screens.home

import com.dev.quickcart.data.model.Product

interface HomeInterActor {

    fun updateSearchInput(it: String)
    fun gotoProductPage(it: Int)
    fun gotoCart()
    suspend fun addToCart(product: Product)
    fun gotoProfile()
    fun gotoAddAddress()


}


