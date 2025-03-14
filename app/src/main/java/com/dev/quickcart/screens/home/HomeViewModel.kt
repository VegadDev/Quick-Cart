package com.dev.quickcart.screens.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.data.model.Product
import com.dev.quickcart.data.repository.DataRepository
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val navigator: Navigator,
    @ApplicationContext private val context: Context,
    private val dataRepository: DataRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()



    val interActor = object : HomeInterActor {


        override fun gotoAddProduct() {
            navigator.navigate(NavigationCommand.To(AppScreens.AddScreen.route))
        }

        override fun updateSearchInput(it: String) {
            _uiState.value = _uiState.value.copy(searchInput = it , searchInputError = "")
        }



        override fun gotoProductPage(it: Int) {
            navigator.navigate(NavigationCommand.To(AppScreens.ProductPageScreen.route , Gson().toJson(it) ))
        }

        override fun gotoCart() {
            navigator.navigate(NavigationCommand.To(AppScreens.CartScreen.route))
        }

        override fun addToCart(product: Product) {
            viewModelScope.launch {
                dataRepository.addToCart(product)
            }
        }

        override fun gotoProfile() {
            navigator.navigate(NavigationCommand.To(AppScreens.ProfileScreen.route))
        }

    }

    init {
        fetchGoogleAccountData()
//        viewModelScope.launch {
//            invoke().collect { items ->
//                _uiState.value = HomeUiState(items)
//            }
//            fetchGoogleAccountData()
//        }

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

    operator fun invoke(): Flow<List<Product>> = dataRepository.getAllProduct()



}