package com.dev.quickcart.screens.productPage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.data.repository.DataRepository
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductPageViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val dataRepository: DataRepository
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
            val product = dataRepository.getProductById(productPageId)
            _uiState.value = _uiState.value.copy(
                productName = product.prodName,
                productImage = product.prodImage,
                productPrice = product.prodPrice,
                prodDescription = product.prodDescription,
                productTypeValue = product.productTypeValue,
                prodCalories = product.prodCalories,
                prodProtein = product.prodProtein,
                prodFat = product.prodFat,
                prodFiber = product.prodFiber
            )
        }

    }


}