package com.dev.quickcart.screens.profile.edit_profile

data class EditProfileUiState(

    val editName: String = "",
    val editNameError: String? = null,

    val editEmail: String = "",
    val editEmailError: String? = null,

    val mobileNumber: String = "",
    val mobileNumberError: String? = null,

)
