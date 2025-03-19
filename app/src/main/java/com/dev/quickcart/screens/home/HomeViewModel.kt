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
import com.dev.quickcart.data.model.CartItem
import com.dev.quickcart.data.model.Product
import com.dev.quickcart.data.repository.NetworkRepository
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
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

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val navigator: Navigator,
    private val networkRepository: NetworkRepository,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
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

        override suspend fun addToCart(product: Product) {
            addToCartItem(product)
        }

        override fun gotoProfile() {
            navigator.navigate(NavigationCommand.To(AppScreens.ProfileScreen.route))
        }

    }

    init {
        fetchGoogleAccountData()
        fetchAllProducts()
        observeCartItems()
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


    fun addToCartItem(product: Product) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                _uiState.value = HomeUiState(error = "User not signed in")
                return@launch
            }

            val cartItem = CartItem(
                productId = product.prodId.toString(),
                productName = product.prodName,
                productPrice = product.prodPrice.toDouble(),
                quantity = 1
            )

            val result = networkRepository.addToCart(userId, cartItem)
            if (result.isSuccess) {
                //fetchCartItems() // Refresh the cart
            } else {
                _uiState.value = HomeUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }



    private fun observeCartItems() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            networkRepository.getCartItems(userId).collect { result ->
                if (result.isSuccess) {
                    val cartItems = result.getOrNull() ?: emptyList()
                    val totalCount = cartItems.sumOf { it.quantity }
                    uiState.value.cartCount = totalCount
                } else {
                    println("Error fetching cart items: ${result.exceptionOrNull()?.message}")
                }
            }
        }
    }


}