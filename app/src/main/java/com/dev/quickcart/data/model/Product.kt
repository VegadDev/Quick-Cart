package com.dev.quickcart.data.model

import androidx.compose.runtime.Stable
import com.google.firebase.firestore.Blob

@Stable
data class Product(

    val prodId: Int = 0,
    val prodName: String,
    val prodImage: Blob? = null,
    val prodPrice: String,
    val prodDescription: String = "",
    val productCategory: String = "",
    val productType: String = "",
    val productTypeValue: String = "",
    val prodCalories: String = "",
    val prodProtein: String = "",
    val prodFat: String = "",
    val prodFiber: String = "",
    val lastModified: Long
){
    constructor() : this(
        0,
        "",
        null,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0L
    ) // No-arg constructor required for Firestore
}
