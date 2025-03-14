package com.dev.quickcart.navigation


import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.quickcart.screens.addProduct.AddProductScreen
import com.dev.quickcart.screens.addProduct.AddProductViewModel
import com.dev.quickcart.screens.login.intro.IntroScreen
import com.dev.quickcart.screens.login.intro.IntroViewModel
import com.dev.quickcart.screens.login.login_screen.LoginScreen
import com.dev.quickcart.screens.login.login_screen.LoginViewModel
import com.dev.quickcart.screens.login.otp_verification.OtpScreen
import com.dev.quickcart.screens.login.otp_verification.OtpViewModel
import com.dev.quickcart.screens.cart.CartScreen
import com.dev.quickcart.screens.cart.CartViewModel
import com.dev.quickcart.screens.categories.CategoriesScreen
import com.dev.quickcart.screens.categories.CategoriesViewModel
import com.dev.quickcart.screens.home.HomeScreen
import com.dev.quickcart.screens.home.HomeViewModel
import com.dev.quickcart.screens.login.splash_screen.SplashScreen
import com.dev.quickcart.screens.login.splash_screen.SplashScreenViewModel
import com.dev.quickcart.screens.productPage.ProductPageScreen
import com.dev.quickcart.screens.productPage.ProductPageViewModel
import com.dev.quickcart.screens.profile.ProfileScreen
import com.dev.quickcart.screens.profile.ProfileViewModel
import com.dev.quickcart.screens.profile.edit_profile.EditProfileScreen
import com.dev.quickcart.screens.profile.edit_profile.EditProfileViewModel
import com.dev.quickcart.screens.settings.SettingScreen
import com.dev.quickcart.screens.settings.SettingViewModel

sealed class AppScreens(val route: String) {

    data object HomeScreen : AppScreens("HomeScreen")
    data object CartScreen : AppScreens("CartScreen")
    data object ProfileScreen : AppScreens("ProfileScreen")
    data object EditProfileScreen : AppScreens("EditProfileScreen")
    data object SettingScreen : AppScreens("SettingScreen")
    data object CategoriesScreen : AppScreens("CategoriesScreen")
    data object SplashScreen : AppScreens("SplashScreen")
    data object IntroScreen : AppScreens("IntroScreen")
    data object LoginScreen : AppScreens("LoginScreen")
    data object OtpScreen : AppScreens("OtpScreen")
    data object AddScreen : AppScreens("AddScreen")
    data object ProductPageScreen : AppScreens("ProductPageScreen")

}


val screens = listOf(

    Screen(AppScreens.HomeScreen.route) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        HomeScreen(interActor = viewModel.interActor , uiState)
    },
    Screen(AppScreens.CartScreen.route) {
        val viewModel = hiltViewModel<CartViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        CartScreen(interActor = viewModel.interActor , uiState)
    },
    Screen(AppScreens.ProfileScreen.route) {
        val viewModel = hiltViewModel<ProfileViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        ProfileScreen(interActor = viewModel.interActor , uiState)
    },
    Screen(AppScreens.EditProfileScreen.route) {
        val viewModel = hiltViewModel<EditProfileViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        EditProfileScreen(interActor = viewModel.interActor , uiState)
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
    Screen(AppScreens.SplashScreen.route) {
        val viewModel = hiltViewModel<SplashScreenViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        SplashScreen(uiState)
    },
    Screen(AppScreens.LoginScreen.route) {
        val viewModel = hiltViewModel<LoginViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        LoginScreen(interActor = viewModel.interActor , uiState)
    },
    Screen(AppScreens.OtpScreen.route) {
        val viewModel = hiltViewModel<OtpViewModel>()
        OtpScreen(interActor = viewModel.interActor)
    },
    Screen(AppScreens.AddScreen.route) {
        val viewModel = hiltViewModel<AddProductViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        AddProductScreen(interActor = viewModel.interActor,uiState)
    },
    Screen(AppScreens.ProductPageScreen.route) {
        val viewModel = hiltViewModel<ProductPageViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        ProductPageScreen(interActor = viewModel.interActor,uiState)
    },




)