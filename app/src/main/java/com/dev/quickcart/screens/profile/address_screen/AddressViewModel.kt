package com.dev.quickcart.screens.profile.address_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dev.quickcart.data.repository.NetworkRepository
import com.dev.quickcart.navigation.Navigator
import com.dev.quickcart.screens.profile.ProfileInterActor
import com.dev.quickcart.screens.profile.ProfileUiState
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddressViewModel
@Inject
constructor(
    private val navigator: Navigator,
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState: StateFlow<AddressUiState> = _uiState.asStateFlow()

    val interActor = object : AddressInterActor {


    }


}