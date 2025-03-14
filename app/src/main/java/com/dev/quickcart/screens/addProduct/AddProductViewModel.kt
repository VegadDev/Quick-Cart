package com.dev.quickcart.screens.addProduct

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.data.model.Product
import com.dev.quickcart.data.repository.DataRepository
import com.dev.quickcart.utils.Status
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddProductViewModel
@Inject
constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState = _uiState.asStateFlow()


    val interActor = object : AddProductInterActor {

        override fun updateProdName(it: String) {
            _uiState.value = _uiState.value.copy(productName = it, productNameError = "")
        }

        override fun updateProdImage(it: List<String>) {
            _uiState.value = _uiState.value.copy(productImage = it, productImageError = "")
        }

        override fun updateProdPrice(it: String) {
            _uiState.value = _uiState.value.copy(productPrice = it, productPriceError = "")
        }

        override fun updateProdDescription(it: String) {
            _uiState.value = _uiState.value.copy(productDescription = it, productDescriptionError = "")
        }

//        override fun updateProdCategory(it: String) {
//            _uiState.value = _uiState.value.copy(productCategory = it, productCategoryError = "")
//        }
//
//        override fun updateProdType(it: String) {
//            _uiState.value = _uiState.value.copy(productType = it, productTypeError = "")
//        }

        override fun updateProdTypeValue(it: String) {
            _uiState.value = _uiState.value.copy(productTypeValue = it, productTypeValueError = "")
        }

        override fun updateProdProtein(it: String) {
            _uiState.value = _uiState.value.copy(productProtein = it, productProteinError = "")
        }

        override fun updateProdCalories(it: String) {
            _uiState.value = _uiState.value.copy(productCalories = it, productCaloriesError = "")
        }

        override fun updateProdFat(it: String) {
            _uiState.value = _uiState.value.copy(productFat = it, productFatError = "")
        }

        override fun updateProdFiber(it: String) {
            _uiState.value = _uiState.value.copy(productFiber = it, productFiberError = "")
        }

        override fun submit() {
            if (uiState.value.productName.isEmpty()) {
                _uiState.value = _uiState.value.copy(productNameError = "Please enter Product Name")
            } else if (uiState.value.productImage.isEmpty()) {
                _uiState.value = _uiState.value.copy(productImageError = "Please select Product Image")
            } else if (uiState.value.productPrice.isEmpty()) {
                _uiState.value = _uiState.value.copy(productPriceError = "Please enter Product Price")
            } else if (uiState.value.productDescription.isEmpty()) {
                _uiState.value = _uiState.value.copy(productDescriptionError = "Please enter Product Description")
            } else if (uiState.value.productTypeValue.isEmpty()) {
                _uiState.value = _uiState.value.copy(productTypeValueError = "Please enter Product Type Value")
            } else if (uiState.value.productProtein.isEmpty()) {
                _uiState.value = _uiState.value.copy(productProteinError = "Please enter Product Protein")
            } else if (uiState.value.productCalories.isEmpty()) {
                _uiState.value = _uiState.value.copy(productCaloriesError = "Please enter Product Calories")
            } else if (uiState.value.productFat.isEmpty()) {
                _uiState.value = _uiState.value.copy(productFatError = "Please enter Product Fat")
            } else if (uiState.value.productFiber.isEmpty()) {
                _uiState.value = _uiState.value.copy(productFiberError = "Please enter Product Fiber")
            }

            else {
                viewModelScope.launch {
                    dataRepository.upsertProduct(
                        Product(
                            prodName = uiState.value.productName,
                            prodImage = Gson().toJson(uiState.value.productImage),
                            prodPrice = uiState.value.productPrice,
                            prodDescription = uiState.value.productDescription,
                            productCategory = uiState.value.productCategory,
                            productType = uiState.value.productType,
                            productTypeValue = uiState.value.productTypeValue,
                            prodCalories = uiState.value.productCalories,
                            prodProtein = uiState.value.productProtein,
                            prodFat = uiState.value.productFat,
                            prodFiber = uiState.value.productFiber,
                            lastModified = System.currentTimeMillis()
                        )
                    ).collect { dataState ->
                        when (dataState.status) {
                            Status.LOADING -> {
                                // Show loading state (e.g., update UI state)
                                //_uiState.value = uiState.value.copy(isLoading = true)
                            }
                            Status.SUCCESS -> {
                                // Handle success, update UI, or show a success message
                                _uiState.value = uiState.value.copy(
                                    //isLoading = false,
                                    successMessage = "Product saved successfully!",
                                    productName = "",
                                    productImage = emptyList(),
                                    productPrice = "",
                                    productDescription = "",
                                    productCategory = "",
                                    productType = "",
                                    productTypeValue = "",
                                    productProtein = "",
                                    productCalories = "",
                                    productFat = "",
                                    productFiber = ""
                                )
                            }
                            Status.FAILURE -> {
                                // Handle error case, update UI state with error message
                                _uiState.value = uiState.value.copy(
                                    //isLoading = false,
                                    errorMessage = dataState.message ?: "Something went wrong!"
                                )
                            }
                            Status.INITIAL -> {}
                        }
                    }
                }
            }
        }


    }
}

