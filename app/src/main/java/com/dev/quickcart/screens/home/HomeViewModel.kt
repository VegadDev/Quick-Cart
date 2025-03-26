package com.dev.quickcart.screens.home

import android.content.Context
import android.util.Log
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
import com.google.firebase.firestore.ListenerRegistration
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
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _loadingStates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val loadingStates: StateFlow<Map<String, Boolean>> = _loadingStates.asStateFlow()

    private var cartListener: ListenerRegistration? = null


    val interActor = object : HomeInterActor {


        override fun updateSearchInput(it: String) {
            _uiState.value = _uiState.value.copy(searchInput = it, searchInputError = "")
        }


        override fun gotoProductPage(it: Int) {
            viewModelScope.launch {
                delay(250)
                navigator.navigate(NavigationCommand.To(AppScreens.ProductPageScreen.route, Gson().toJson(it)))
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

        override fun gotoAddAddress() {
            navigator.navigate(NavigationCommand.To(AppScreens.GetProfileScreen.route))
        }


    }

    init {
        fetchGoogleAccountData()
        fetchAllProducts()
        startCartListener()
        fetchAddresses()
    }

    override fun onCleared() {
        cartListener?.remove()
        super.onCleared()
    }

    private fun fetchGoogleAccountData() {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            _uiState.update {
                it.copy(
                    //userName = account.displayName ?: "Guest",
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
            delay(500)
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

            // Optimistic update: Increment cart count locally
            val newCartCount = _uiState.value.cartCount + 1
            _uiState.value = _uiState.value.copy(cartCount = newCartCount)

            // Set loading state for this product
            _loadingStates.value = _loadingStates.value.toMutableMap().apply {
                put(product.prodId.toString(), true)
            }

            val cartItem = CartItem(
                productId = product.prodId.toString(),
                productName = product.prodName,
                productPrice = product.prodPrice.toDouble(),
                productImage = product.prodImage,
                productType = product.productType,
                productTypeValue = product.productTypeValue,
                quantity = 1
            )

            val result = networkRepository.addOrUpdateCartItem(userId, cartItem)
            if (result.isFailure) {
                // Rollback on error
                _uiState.value = _uiState.value.copy(
                    cartCount = _uiState.value.cartCount - 1,
                    error = result.exceptionOrNull()?.message
                )
            }

            // Clear loading state (no delay needed)
            _loadingStates.value = _loadingStates.value.toMutableMap().apply {
                put(product.prodId.toString(), false)
            }
        }
    }


    private fun startCartListener() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            cartListener = networkRepository.listenToCartItems(userId) { cartItems ->
                _uiState.value = _uiState.value.copy(cartItems = cartItems)
            }
        }
    }

    private fun fetchAddresses() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            networkRepository.getUserAddresses(userId).collect { result ->
                when {
                    result.isSuccess -> {
                        val addresses = result.getOrNull() ?: emptyList()
                        val username = addresses.firstOrNull()?.username ?: auth.currentUser?.displayName ?: "User"
                        _uiState.update { it.copy(addresses = addresses, userName = username) }
                    }
                    result.isFailure -> {
                        _uiState.update { it.copy(error = result.exceptionOrNull()?.message) }
                    }
                }
            }
        }
    }




}