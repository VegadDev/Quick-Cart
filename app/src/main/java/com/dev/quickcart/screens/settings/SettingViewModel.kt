package com.dev.quickcart.screens.settings

import androidx.lifecycle.ViewModel
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
@Inject
constructor(
    private val navigator: Navigator
) : ViewModel() {

    val interActor = object : SettingInterActor {
        override fun gotoCategory() {
            navigator.navigate(NavigationCommand.To(AppScreens.CategoriesScreen.route))
        }


    }
}