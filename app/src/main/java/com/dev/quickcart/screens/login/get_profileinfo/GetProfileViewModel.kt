package com.dev.quickcart.screens.login.get_profileinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.admin.AdminUiState
import com.dev.quickcart.data.model.UserAddress
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class GetProfileViewModel
@Inject
constructor(
    private val navigator: Navigator,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(GetProfileUiState())
    val uiState: StateFlow<GetProfileUiState> = _uiState.asStateFlow()

    val interActor = object : GetProfileInterActor {
        override fun updateUserName(string: String) {
            _uiState.update { it.copy(userName = string, userNameError = "") }
        }

        override fun updateMobileNumber(string: String) {
            _uiState.update { it.copy(mobileNumber = string, mobileNumberError = "") }
        }

        override fun updateHouseAddress(string: String) {
            _uiState.update { it.copy(houseAddress = string, houseAddressError = "") }
        }

        override fun updateAreaAddress(string: String) {
            _uiState.update { it.copy(areaAddress = string, areaAddressError = "") }
        }

        override fun updateLandmarkAddress(string: String) {
            _uiState.update { it.copy(landmarkAddress = string, landmarkAddressError = "") }
        }

        override fun updateCategory(string: String) {
            _uiState.update { it.copy(category = string) }
        }

        override fun submit() {
            if (_uiState.value.userName.isEmpty()){
                _uiState.update { it.copy(userNameError = "Enter User Name") }
            }
            else if (_uiState.value.mobileNumber.isEmpty()){
                _uiState.update { it.copy(mobileNumberError = "Enter Mobile Number") }
            }
            else if (_uiState.value.houseAddress.isEmpty()){
                _uiState.update { it.copy(houseAddressError = "Enter House Address") }
            }
            else if (_uiState.value.areaAddress.isEmpty()){
                _uiState.update { it.copy(areaAddressError = "Enter Area Address") }
            }
            else if (_uiState.value.category.isEmpty()){
                _uiState.update { it.copy(categoryError = "Select Category") }
            }
            else {
                saveAddress()
                _uiState.update { it.copy(
                    isSaved = true,
                    userName = "",
                    mobileNumber = "",
                    houseAddress = "",
                    areaAddress = "",
                    landmarkAddress = "",
                    category = ""
                ) }
                navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.HomeScreen.route))
            }
        }

    }

    fun saveAddress() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            _uiState.value = _uiState.value.copy(isLoading = true)

            val address = UserAddress(
                username = _uiState.value.userName,
                phoneNumber = _uiState.value.mobileNumber,
                houseAddress = _uiState.value.houseAddress,
                areaAddress = _uiState.value.areaAddress,
                landmark = _uiState.value.landmarkAddress.takeIf { it.isNotBlank() },
                category = _uiState.value.category
            )

            try {
                // Save under users/{userId}/addresses/{category}
                firestore.collection("users")
                    .document(userId)
                    .collection("addresses")
                    .document(address.category)
                    .set(address)
                    .await()
                _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }



}