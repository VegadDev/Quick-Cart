package com.dev.quickcart.screens.login.login_screen

import androidx.lifecycle.ViewModel
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.dev.quickcart.screens.home.HomeUiState
import com.dev.quickcart.screens.settings.SettingInterActor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    val interActor = object : LoginInterActor {
        override fun updateNumber(it: String) {
            _uiState.value = _uiState.value.copy(numberInput = it , numberInputError = "")
        }

        override fun gotoHomeScreen() {
            navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.HomeScreen.route))
        }


    }

}