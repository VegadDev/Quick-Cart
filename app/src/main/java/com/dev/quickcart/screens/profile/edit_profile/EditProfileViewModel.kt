package com.dev.quickcart.screens.profile.edit_profile

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.toInt


@HiltViewModel
class EditProfileViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    @ApplicationContext private val context: Context
) : ViewModel() {

    //val editProfileId = savedStateHandle.get<String>("json")?.toInt()?: throw IllegalStateException("User Id is null")

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    val interActor = object : EditProfileInterActor {

        override fun updateUserName(it: String) {
            _uiState.update { it.copy(editName = it.editName , editNameError = "") }
        }

        override fun updateEmail(it: String) {
            _uiState.update { it.copy(editEmail = it.editEmail , editEmailError = "") }
        }

        override fun updateMobileNumber(it: String) {
            _uiState.update { it.copy(mobileNumber = it.mobileNumber , mobileNumberError = "") }
        }

    }



}