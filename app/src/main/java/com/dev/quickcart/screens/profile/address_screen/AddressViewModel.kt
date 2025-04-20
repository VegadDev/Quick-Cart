package com.dev.quickcart.screens.profile.address_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.data.repository.NetworkRepository
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel
@Inject
constructor(
    private val navigator: Navigator,
    private val networkRepository: NetworkRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState: StateFlow<AddressUiState> = _uiState.asStateFlow()

    val interActor = object : AddressInterActor {
        override fun onBackClick() {
            navigator.navigate(NavigationCommand.Back)
        }

        override fun onAddAddressClick() {
            navigator.navigate(NavigationCommand.To(AppScreens.GetProfileScreen.route))
        }

    }

    init {
        fetchUserAddresses()
    }

    private fun fetchUserAddresses() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            networkRepository.getUserAddresses(userId).collect { result ->
                when {
                    result.isSuccess -> {
                        _uiState.value.addresses = result.getOrNull() ?: emptyList()
                    }
                    result.isFailure -> {
                        Log.e("AddressViewModel", "Failed to fetch addresses: ${result.exceptionOrNull()?.message}")
                    }
                }
            }
        }
    }

}