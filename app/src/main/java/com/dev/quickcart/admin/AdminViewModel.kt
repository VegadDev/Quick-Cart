package com.dev.quickcart.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.dev.quickcart.screens.home.HomeInterActor
import com.dev.quickcart.screens.home.HomeUiState
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AdminViewModel
@Inject
constructor(
    private val googleSignInClient: GoogleSignInClient,
    private val firebaseAuth: FirebaseAuth,
    private val navigator: Navigator,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()



    val interActor = object : AdminInterActor {
        override fun gotoAddProduct() {
            navigator.navigate(NavigationCommand.To(AppScreens.AddScreen.route))
        }

        override fun logout() {
            logoutAdmin()
        }


    }

    private fun logoutAdmin() {
        firebaseAuth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            //_uiState.update { it.copy(isLoggingOut = true) }
            navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.LoginScreen.route))
        }
    }

}