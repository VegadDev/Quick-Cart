package com.dev.quickcart.screens.login.login_screen

data class LoginUiState(

    val numberInput: String = "",
    val numberInputError: String = "",

    val isLoading: Boolean = false,
    val userEmail: String? = null,
    val error: String? = null

)
