package com.dev.quickcart.navigation


import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.quickcart.screens.cart.CartScreen
import com.dev.quickcart.screens.cart.CartViewModel
import com.dev.quickcart.screens.home.HomeScreen
import com.dev.quickcart.screens.home.HomeViewModel
import com.dev.quickcart.screens.profile.ProfileScreen
import com.dev.quickcart.screens.profile.ProfileViewModel

sealed class AppScreens(val route: String) {

    data object HomeScreen : AppScreens("HomeScreen")
    data object CartScreen : AppScreens("CartScreen")
    data object ProfileScreen : AppScreens("ProfileScreen")

}


val screens = listOf(

    Screen(AppScreens.HomeScreen.route) {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeScreen(interActor = viewModel.interActor)
    },
    Screen(AppScreens.CartScreen.route) {
        val viewModel = hiltViewModel<CartViewModel>()
        CartScreen(interActor = viewModel.interActor)
    },
    Screen(AppScreens.ProfileScreen.route) {
        val viewModel = hiltViewModel<ProfileViewModel>()
        ProfileScreen(interActor = viewModel.interActor)
    }


)