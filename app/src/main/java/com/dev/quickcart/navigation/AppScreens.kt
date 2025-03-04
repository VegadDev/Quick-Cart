package com.dev.quickcart.navigation


import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.quickcart.login.intro.IntroScreen
import com.dev.quickcart.login.intro.IntroViewModel
import com.dev.quickcart.login.login_screen.LoginScreen
import com.dev.quickcart.login.login_screen.LoginViewModel
import com.dev.quickcart.login.signup_screen.SignupScreen
import com.dev.quickcart.login.signup_screen.SignupViewModel
import com.dev.quickcart.screens.cart.CartScreen
import com.dev.quickcart.screens.cart.CartViewModel
import com.dev.quickcart.screens.categories.CategoriesScreen
import com.dev.quickcart.screens.categories.CategoriesViewModel
import com.dev.quickcart.screens.home.HomeScreen
import com.dev.quickcart.screens.home.HomeViewModel
import com.dev.quickcart.screens.profile.ProfileScreen
import com.dev.quickcart.screens.profile.ProfileViewModel
import com.dev.quickcart.screens.settings.SettingScreen
import com.dev.quickcart.screens.settings.SettingViewModel

sealed class AppScreens(val route: String) {

    data object HomeScreen : AppScreens("HomeScreen")
    data object CartScreen : AppScreens("CartScreen")
    data object ProfileScreen : AppScreens("ProfileScreen")
    data object SettingScreen : AppScreens("SettingScreen")
    data object CategoriesScreen : AppScreens("CategoriesScreen")
    data object IntroScreen : AppScreens("IntroScreen")
    data object LoginScreen : AppScreens("LoginScreen")
    data object SignupScreen : AppScreens("SignupScreen")

}


val screens = listOf(

    Screen(AppScreens.HomeScreen.route) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        HomeScreen(interActor = viewModel.interActor , uiState)
    },
    Screen(AppScreens.CartScreen.route) {
        val viewModel = hiltViewModel<CartViewModel>()
        CartScreen(interActor = viewModel.interActor)
    },
    Screen(AppScreens.ProfileScreen.route) {
        val viewModel = hiltViewModel<ProfileViewModel>()
        ProfileScreen(interActor = viewModel.interActor)
    },
    Screen(AppScreens.SettingScreen.route) {
        val viewModel = hiltViewModel<SettingViewModel>()
        SettingScreen(interActor = viewModel.interActor)
    },
    Screen(AppScreens.CategoriesScreen.route) {
        val viewModel = hiltViewModel<CategoriesViewModel>()
        CategoriesScreen(interActor = viewModel.interActor)
    },
    Screen(AppScreens.IntroScreen.route) {
        val viewModel = hiltViewModel<IntroViewModel>()
        IntroScreen(interActor = viewModel.interActor)
    },
    Screen(AppScreens.LoginScreen.route) {
        val viewModel = hiltViewModel<LoginViewModel>()
        LoginScreen(interActor = viewModel.interActor)
    },
    Screen(AppScreens.SignupScreen.route) {
        val viewModel = hiltViewModel<SignupViewModel>()
        SignupScreen(interActor = viewModel.interActor)
    },



)