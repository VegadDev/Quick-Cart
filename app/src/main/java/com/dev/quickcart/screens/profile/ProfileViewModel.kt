package com.dev.quickcart.screens.profile

import androidx.lifecycle.ViewModel
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.dev.quickcart.screens.cart.CartInterActor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val navigator: Navigator
) : ViewModel() {

    val interActor = object : ProfileInterActor {
        override fun gotoHome() {
            navigator.navigate(NavigationCommand.Back)
        }

    }

}