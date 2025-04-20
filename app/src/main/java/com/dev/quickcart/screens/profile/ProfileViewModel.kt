package com.dev.quickcart.screens.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.google.android.gms.auth.api.signin.GoogleSignIn
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
class ProfileViewModel
@Inject
constructor(
    private val googleSignInClient: GoogleSignInClient,
    private val firebaseAuth: FirebaseAuth,
    private val navigator: Navigator,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    val interActor = object : ProfileInterActor {



        override fun onLogoutClick() {
            logout()
        }

        override fun onOrdersClick() {
            navigator.navigate(NavigationCommand.To(AppScreens.OrderScreen.route))
        }

        override fun onBackClick() {
            navigator.navigate(NavigationCommand.Back)
        }

        override fun onAddressesClick() {
            navigator.navigate(NavigationCommand.To(AppScreens.AddressScreen.route))
        }

    }

    //Gson().toJson(it)

    private fun logout() {
        firebaseAuth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            _uiState.update { it.copy(isLoggingOut = true) }
            navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.LoginScreen.route))
        }
    }

    init {
        fetchGoogleAccountData()
    }



    private fun fetchGoogleAccountData() {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            _uiState.update {
                it.copy(
                    userName = account.displayName ?: "Guest",
                    userImage = account.photoUrl?.toString(),
                    userId = account.email.toString()
                )
            }
        } else {
            navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.LoginScreen.route))
        }
    }


}