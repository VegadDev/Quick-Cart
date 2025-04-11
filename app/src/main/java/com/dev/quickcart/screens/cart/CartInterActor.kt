package com.dev.quickcart.screens.cart

interface CartInterActor{
    fun onBackClick()

    fun onIncrementClick(productId: String)
    fun onDecrementClick(productId: String)
    fun onRemoveClick(productId: String)
    fun proceedToCheckout()


}

