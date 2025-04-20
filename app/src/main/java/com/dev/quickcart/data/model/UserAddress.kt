package com.dev.quickcart.data.model

import androidx.compose.runtime.Stable


@Stable
data class UserAddress(

    val username: String = "",
    val phoneNumber: String = "",
    val houseAddress: String = "",
    val areaAddress: String = "",
    val landmark: String? = null,
    val category: String = "",

    )
