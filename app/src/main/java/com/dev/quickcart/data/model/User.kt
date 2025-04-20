package com.dev.quickcart.data.model

import androidx.compose.runtime.Stable


@Stable
data class User(

    val userId: String,
    val username: String?,
    val profileImage: String?,

)
