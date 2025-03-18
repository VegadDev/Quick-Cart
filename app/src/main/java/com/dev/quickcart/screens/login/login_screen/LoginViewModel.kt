package com.dev.quickcart.screens.login.login_screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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
class LoginViewModel @Inject constructor(
    private val navigator: Navigator,
    private val googleSignInClient: GoogleSignInClient,
    private val firebaseAuth: FirebaseAuth?,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val adminEmail = "vegaddevdatt@gmail.com"

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    val interActor: LoginInterActor = object : LoginInterActor {
        override fun updateNumber(number: String) {
            _uiState.update { it.copy(numberInput = number) }
        }

        override fun onGoogleSignInClick() {
            startGoogleSignIn()
        }

        override fun handleGoogleSignInResult(result: ActivityResult) {
            processGoogleSignInResult(result)
        }

        override fun setGoogleSignInLauncher(launcher: ActivityResultLauncher<Intent>) {
            googleSignInLauncher = launcher
        }
    }

    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    private fun startGoogleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        _uiState.update { it.copy(isLoading = true) }
        try {
            googleSignInLauncher.launch(signInIntent)
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Launch failed: ${e.message}", isLoading = false) }
        }
    }

    private fun processGoogleSignInResult(result: ActivityResult) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (result.resultCode == Activity.RESULT_OK) {
                firebaseAuth?.let { auth ->
                    firebaseAuthWithGoogle(account.idToken ?: return)
                } ?: onSignInSuccess(account)
            } else {
                _uiState.update { it.copy(error = "Sign-In Cancelled", isLoading = false) }
            }
        } catch (e: ApiException) {
            _uiState.update { it.copy(error = "Sign-In Failed: ${e.statusCode}", isLoading = false) }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Error: ${e.message}", isLoading = false) }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignInSuccess(GoogleSignIn.getLastSignedInAccount(context))
            } else {
                _uiState.update { it.copy(error = "Firebase Auth Failed", isLoading = false) }
            }
        }
    }

    private fun onSignInSuccess(account: GoogleSignInAccount?) {
        _uiState.update { it.copy(isLoading = false, userEmail = account?.email) }
        if (account?.email == adminEmail){
            navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.AdminScreen.route))
        }
        else {
            navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.HomeScreen.route))
        }
    }
}

