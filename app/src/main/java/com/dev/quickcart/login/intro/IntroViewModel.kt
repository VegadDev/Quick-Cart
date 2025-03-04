package com.dev.quickcart.login.intro

import androidx.lifecycle.ViewModel
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.dev.quickcart.screens.settings.SettingInterActor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class IntroViewModel
@Inject
constructor(
    private val navigator: Navigator
) : ViewModel() {

    val interActor = object : IntroInterActor {
        override fun gotoLogin() {
            navigator.navigate(NavigationCommand.To(AppScreens.LoginScreen.route))
        }

    }


}