package com.dev.quickcart.screens.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.data.model.Product
import com.dev.quickcart.data.repository.NetworkRepository
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.gson.Gson
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
class HomeViewModel
@Inject
constructor(
    private val navigator: Navigator,
    private val networkRepository: NetworkRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()



    val interActor = object : HomeInterActor {


        override fun updateSearchInput(it: String) {
            _uiState.value = _uiState.value.copy(searchInput = it , searchInputError = "")
        }



        override fun gotoProductPage(it: Int) {
            viewModelScope.launch {
                delay(200)
                navigator.navigate(NavigationCommand.To(AppScreens.ProductPageScreen.route , Gson().toJson(it) ))
            }

        }

        override fun gotoCart() {
            navigator.navigate(NavigationCommand.To(AppScreens.CartScreen.route))
        }

        override fun addToCart(product: Product) {

        }

        override fun gotoProfile() {
            navigator.navigate(NavigationCommand.To(AppScreens.ProfileScreen.route))
        }

    }

    init {
        fetchGoogleAccountData()
        fetchAllProducts()
    }


    private fun fetchGoogleAccountData() {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            _uiState.update {
                it.copy(
                    userName = account.displayName ?: "Guest",
                    userImage = account.photoUrl?.toString()
                )
            }
        } else {
            navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.LoginScreen.route))
        }
    }



    fun fetchAllProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            delay(100)
            val result = networkRepository.getAllProducts()
            _uiState.update { currentState ->
                when {
                    result.isSuccess -> currentState.copy(
                        isLoading = false,
                        productList = result.getOrNull() ?: emptyList(),
                        error = null
                    )
                    result.isFailure -> currentState.copy(
                        isLoading = true,
                        error = result.exceptionOrNull()?.message
                    )
                    else -> currentState
                }
            }
        }
    }


}