package com.dev.quickcart.screens.productPage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.data.model.Product
import com.dev.quickcart.data.repository.NetworkRepository
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val networkRepository: NetworkRepository
) : ViewModel() {

    val productPageId = savedStateHandle.get<String>("json")?.toInt()?: throw IllegalStateException("Product Id is null")

    private val _uiState = MutableStateFlow(ProductPageUiState())
    val uiState: StateFlow<ProductPageUiState> = _uiState.asStateFlow()


    val interActor = object : ProductPageInterActor {

        override fun onBackClick() {
            navigator.navigate(NavigationCommand.Back)
        }

    }

    init {
        viewModelScope.launch {
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




}