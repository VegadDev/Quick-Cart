package com.dev.quickcart.screens.profile.address_screen

import com.dev.quickcart.data.model.UserAddress

data class AddressUiState(

    var addresses: List<UserAddress> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,

    val userName: String = "",
    val mobileNumber: String = "",

    )
