package com.dev.quickcart.screens.login.get_profileinfo

data class GetProfileUiState(


    val userName: String = "",
    val userNameError: String? = null,

    val mobileNumber: String = "",
    val mobileNumberError: String? = null,

    val houseAddress: String = "",
    val houseAddressError: String? = null,

    val areaAddress: String = "",
    val areaAddressError: String? = null,

    val landmarkAddress: String = "",
    val landmarkAddressError: String? = null,

    val category: String = "Home",
    val categoryError: String? = null,

    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null


)
