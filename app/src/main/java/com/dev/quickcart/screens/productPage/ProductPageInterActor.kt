package com.dev.quickcart.screens.productPage

import com.dev.quickcart.data.model.Product

interface ProductPageInterActor {

    fun onBackClick()

    fun onAddToCartClick(product: Product, quantity: Int)


}