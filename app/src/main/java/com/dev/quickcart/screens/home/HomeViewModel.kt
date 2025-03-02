package com.dev.quickcart.screens.home

import androidx.lifecycle.ViewModel
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.dev.quickcart.navigation.Screen
import com.dev.quickcart.screens.cart.CartInterActor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val navigator: Navigator
) : ViewModel() {

    val interActor = object : HomeInterActor {
        override fun gotoProfile() {
            navigator.navigate(NavigationCommand.To(AppScreens.ProfileScreen.route))
        }

    }

}