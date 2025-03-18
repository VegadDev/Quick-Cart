package com.dev.quickcart.screens.addProduct

import android.net.Uri
import com.dev.quickcart.data.model.Product

data class AddProductUiState(

    val product: Product? = null,

    val productName: String = "",
    val productNameError: String = "",

    var productImage: Uri = Uri.EMPTY,
    val productImageError: String = "",

    val productPrice: String = "",
    val productPriceError: String = "",

    val productDescription: String = "",
    val productDescriptionError: String = "",

    val productCategory: String = "",
    val productCategoryError: String = "",

    val productType: String = "",
    val productTypeError: String = "",

    val productTypeValue: String = "",
    val productTypeValueError: String = "",

    val productCalories: String = "",
    val productCaloriesError: String = "",

    val productProtein: String = "",
    val productProteinError: String = "",

    val productFat: String = "",
    val productFatError: String = "",

    val productFiber: String = "",
    val productFiberError: String = "",

    val successMessage: String? = "",
    val errorMessage: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,

)
