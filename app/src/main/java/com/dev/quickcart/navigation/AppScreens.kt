package com.dev.quickcart.navigation


import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.quickcart.admin.AdminScreen
import com.dev.quickcart.admin.AdminViewModel
import com.dev.quickcart.screens.addProduct.AddProductScreen
import com.dev.quickcart.screens.addProduct.AddProductViewModel
import com.dev.quickcart.screens.login.intro.IntroScreen
import com.dev.quickcart.screens.login.intro.IntroViewModel
import com.dev.quickcart.screens.login.login_screen.LoginScreen
import com.dev.quickcart.screens.login.login_screen.LoginViewModel
import com.dev.quickcart.screens.login.get_profileinfo.GetProfileScreen
import com.dev.quickcart.screens.login.get_profileinfo.GetProfileViewModel
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
import com.dev.quickcart.screens.profile.address_screen.AddressScreen
import com.dev.quickcart.screens.profile.address_screen.AddressViewModel
import com.dev.quickcart.screens.settings.SettingScreen
import com.dev.quickcart.screens.settings.SettingViewModel

sealed class AppScreens(val route: String) {

    data object AdminScreen : AppScreens("AdminScreen")

    data object HomeScreen : AppScreens("HomeScreen")
    data object CartScreen : AppScreens("CartScreen")
    data object ProfileScreen : AppScreens("ProfileScreen")
    data object SettingScreen : AppScreens("SettingScreen")
    data object CategoriesScreen : AppScreens("CategoriesScreen")
    data object SplashScreen : AppScreens("SplashScreen")
    data object IntroScreen : AppScreens("IntroScreen")
    data object LoginScreen : AppScreens("LoginScreen")
    data object GetProfileScreen : AppScreens("GetProfileScreen")
    data object AddScreen : AppScreens("AddScreen")
    data object ProductPageScreen : AppScreens("ProductPageScreen")
    data object AddressScreen : AppScreens("AddressScreen")

}


val screens = listOf(

    Screen(AppScreens.AdminScreen.route) {
        val viewModel = hiltViewModel<AdminViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        AdminScreen(interActor = viewModel.interActor , uiState)
    },

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
    Screen(AppScreens.GetProfileScreen.route) {
        val viewModel = hiltViewModel<GetProfileViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        GetProfileScreen(interActor = viewModel.interActor , uiState)
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
    Screen(AppScreens.AddressScreen.route) {
        val viewModel = hiltViewModel<AddressViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        AddressScreen(interActor = viewModel.interActor,uiState)
    },



)