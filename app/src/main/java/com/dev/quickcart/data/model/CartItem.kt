package com.dev.quickcart.data.model

import com.google.firebase.firestore.Blob

data class CartItem(
    val productId: String = "",
    val productName: String = "",
    val productPrice: Double = 0.0,
    val productImage: Blob? = null,
    val productType: String = "",
    val productTypeValue: Any? = null,
    val quantity: Int = 1
)
