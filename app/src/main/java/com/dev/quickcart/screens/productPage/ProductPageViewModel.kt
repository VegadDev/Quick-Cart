package com.dev.quickcart.screens.productPage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.data.model.CartItem
import com.dev.quickcart.data.model.Product
import com.dev.quickcart.data.repository.NetworkRepository
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductPageViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val auth: FirebaseAuth,
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    val productPageId = savedStateHandle.get<String>("json")?.toInt()
        ?: throw IllegalStateException("Product Id is null")

    private val _uiState = MutableStateFlow(ProductPageUiState())
    val uiState: StateFlow<ProductPageUiState> = _uiState.asStateFlow()

    private val _isAdding = MutableStateFlow(false)
    val isAdding: StateFlow<Boolean> = _isAdding.asStateFlow()


    val interActor = object : ProductPageInterActor {

        override fun onBackClick() {
            navigator.navigate(NavigationCommand.Back)
        }

        override fun onAddToCartClick(product: Product, quantity: Int, ) {
            addToCartItem(product, quantity)
        }

    }

    init {
        viewModelScope.launch {
            delay(300)
            preloadProduct(productPageId.toString())
        }
    }


    fun preloadProduct(productId: String) {
        viewModelScope.launch {
            val result = networkRepository.getProducts(productId)
            _uiState.update {
                it.copy(
                    products = result,
                    isLoading = false
                )
            }
        }
    }


    fun addToCartItem(product: Product, quantity: Int) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            _isAdding.value = true

            val cartItem = CartItem(
                productId = product.prodId.toString(),
                productName = product.prodName,
                productPrice = (product.prodPrice.toInt() / quantity).toDouble(), // Original price per unit
                productImage = product.prodImage,
                productType = product.productType,
                productTypeValue = product.productTypeValue,
                quantity = quantity
            )

            val result = networkRepository.addOrUpdateCartItem(userId, cartItem)
            if (result.isFailure) {
                _uiState.value = _uiState.value.copy(error = result.exceptionOrNull()?.message)
            }
            _isAdding.value = false
        }
    }
}



