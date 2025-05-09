package com.dev.quickcart.screens.login.splash_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@Suppress("DEPRECATION")
@HiltViewModel
class SplashScreenViewModel
@Inject
constructor(
    private val navigator: Navigator,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val networkChecker: NetworkChecker,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val adminEmail = "vegaddevdatt@gmail.com"

    init {
        checkInternetAndSignInStatus()
    }

    fun checkInternetAndSignInStatus1() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, noInternet = false, error = null) }
            if (networkChecker.isInternetAvailable()) {
                try {
                    val account = GoogleSignIn.getLastSignedInAccount(context)
                    delay(500)
                    if (account != null) {
                        if (account.email == adminEmail) {
                            navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.AdminScreen.route))
                        } else {
                            navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.GetProfileScreen.route))
                        }
                    } else {
                        navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.IntroScreen.route))
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, error = "Sign-In Check Failed: ${e.message}") }
                }
            } else {
                _uiState.update { it.copy(isLoading = false, noInternet = true) }
            }
        }
    }



    fun checkInternetAndSignInStatus() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, noInternet = false, error = null) }

            if (networkChecker.isInternetAvailable()) {
                try {
                    val account = GoogleSignIn.getLastSignedInAccount(context)
                    delay(300) // Keep your splash delay

                    if (account != null) {
                        // User is signed in
                        val userId = auth.currentUser?.uid ?: account.id ?: return@launch
                        if (account.email == adminEmail) {
                            // Admin user, go to Admin screen
                            navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.AdminScreen.route))
                        } else {
                            // Regular user, check for addresses
                            val addressesSnapshot = firestore.collection("users")
                                .document(userId)
                                .collection("addresses")
                                .get()
                                .await()
                            val hasAddress = addressesSnapshot.documents.isNotEmpty()

                            if (hasAddress) {
                                navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.HomeScreen.route))
                            } else {
                                navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.GetProfileScreen.route))
                            }
                        }
                    } else {
                        // No signed-in account, go to Intro/Login screen
                        navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.IntroScreen.route))
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, error = "Sign-In Check Failed: ${e.message}") }
                }
            } else {
                _uiState.update { it.copy(isLoading = false, noInternet = true) }
            }
        }
    }




}