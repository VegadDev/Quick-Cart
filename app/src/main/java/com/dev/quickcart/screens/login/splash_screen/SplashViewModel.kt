package com.dev.quickcart.screens.login.splash_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SplashScreenViewModel
@Inject
constructor(
    private val googleSignInClient: GoogleSignInClient,
    private val navigator: Navigator,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()


    init {
        checkSignInStatus()
    }

    private fun checkSignInStatus() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val account = GoogleSignIn.getLastSignedInAccount(context)
            //delay(2000)
            if (account != null) {
                navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.HomeScreen.route))
            } else {
                navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.IntroScreen.route))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}