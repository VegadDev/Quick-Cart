package com.dev.quickcart.data.model

import com.google.firebase.Timestamp


data class Order(
    val userId: String = "",
    val address: UserAddress? = null,
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val orderDate: Timestamp = Timestamp.now(),
    val status: String = "Pending"
){
    // No-arg constructor explicitly defined for Firestore
    constructor() : this("", null, emptyList(), 0.0, Timestamp.now(), "Pending")
}
