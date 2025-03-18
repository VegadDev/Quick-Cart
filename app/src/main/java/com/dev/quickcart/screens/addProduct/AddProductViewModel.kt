package com.dev.quickcart.screens.addProduct

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.CoilUtils.result
import com.dev.quickcart.data.model.Product
import com.dev.quickcart.data.repository.NetworkRepository
import com.dev.quickcart.utils.Status
import com.dev.quickcart.utils.uriToBlob
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.String


@HiltViewModel
class AddProductViewModel
@Inject
constructor(
    private val networkRepository: NetworkRepository,
    @ApplicationContext context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState = _uiState.asStateFlow()


    val interActor = object : AddProductInterActor {

        override fun updateProdName(string: String) {
            _uiState.update { it.copy(productName = string, productNameError = "") }
        }

        override fun updateProdImage(uri: Uri) {
            _uiState.update { it.copy(productImage = uri, productImageError = "") }
        }

        override fun updateProdPrice(string: String) {
            _uiState.update { it.copy(productPrice = string, productPriceError = "") }
        }

        override fun updateProdDescription(string: String) {
            _uiState.update { it.copy(productDescription = string, productDescriptionError = "") }
        }

        override fun updateProdCategory(string: String) {
            _uiState.update { it.copy(productCategory = string, productCategoryError = "") }
        }

        override fun updateProdType(string: String) {
            _uiState.update { it.copy(productType = string, productTypeError = "") }
        }

        override fun updateProdTypeValue(string: String) {
            _uiState.update { it.copy(productTypeValue = string, productTypeValueError = "") }
        }

        override fun updateProdProtein(string: String) {
            _uiState.update { it.copy(productProtein = string, productProteinError = "") }
        }

        override fun updateProdCalories(string: String) {
            _uiState.update { it.copy(productCalories = string, productCaloriesError = "") }
        }

        override fun updateProdFat(string: String) {
            _uiState.update { it.copy(productFat = string, productFatError = "") }
        }

        override fun updateProdFiber(string: String) {
            _uiState.update { it.copy(productFiber = string, productFiberError = "") }
        }

        override fun submit() {
            if (uiState.value.productName.isEmpty()) {
                _uiState.update { it.copy(productNameError = "Please enter Product Name") }
            } else if (uiState.value.productPrice.isEmpty()) {
                _uiState.update { it.copy(productPriceError = "Please enter Product Price") }
            } else if (uiState.value.productDescription.isEmpty()) {
                _uiState.update { it.copy(productDescriptionError = "Please enter Product Description") }
            } else if (uiState.value.productCategory.isEmpty()) {
                _uiState.update { it.copy(productCategoryError = "Please enter Product Category") }
            } else if (uiState.value.productType.isEmpty()) {
                _uiState.update { it.copy(productTypeError = "Please enter Product Type") }
            } else if (uiState.value.productTypeValue.isEmpty()) {
                _uiState.update { it.copy(productTypeValueError = "Please enter Product Type Value") }
            } else if (uiState.value.productProtein.isEmpty()) {
                _uiState.update { it.copy(productProteinError = "Please enter Product Protein") }
            } else if (uiState.value.productCalories.isEmpty()) {
                _uiState.update { it.copy(productCaloriesError = "Please enter Product Calories") }
            } else if (uiState.value.productFat.isEmpty()) {
                _uiState.update { it.copy(productFatError = "Please enter Product Fat") }
            } else if (uiState.value.productFiber.isEmpty()) {
                _uiState.update { it.copy(productFiberError = "Please enter Product Fiber") }
            } else {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            error = null,
                            successMessage = null
                        )
                    }
                    val product = Product(
                        prodName = uiState.value.productName,
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

                    val result = networkRepository.addProduct(product , uiState.value.productImage)
                    Log.d("result", result.toString())

                    _uiState.update {
                        when {
                            result.isSuccess -> it.copy(
                                isLoading = false,
                                successMessage = "Product added with ID: ${result.getOrNull()}",
                                error = null,
                                productName = "",
                                productImage = Uri.EMPTY,
                                productPrice = "",
                                productDescription = "",
                                productCategory = "",
                                productType = "",
                                productTypeValue = "",
                                productCalories = "",
                                productProtein = "",
                                productFat = "",
                                productFiber = ""
                            )

                            result.isFailure -> it.copy(
                                isLoading = false,
                                error = "Failed to add product: ${result.exceptionOrNull()?.message}",
                                successMessage = null
                            )

                            else -> it.copy(isLoading = false)
                        }
                    }
                }
            }
        }


    }
}


suspend fun uriToByteList(context: Context, uri: Uri): List<Byte>? {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024) // 1KB buffer
            var bytesRead: Int
            while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            inputStream?.close()
            outputStream.toByteArray().toList() // Convert ByteArray to List<Byte>
        } catch (e: Exception) {
            Log.e("UriToByteList", "Conversion failed: ${e.message}", e)
            null
        }
    }
}


fun stringToByteArray(imageString: String): ByteArray? {
    return try {
        // Remove square brackets and split by comma
        val byteStrings = imageString.removeSurrounding("[", "]").split(", ")
        val byteList = byteStrings.map { it.trim().toByte() }
        byteList.toByteArray()
    } catch (e: Exception) {
        Log.e("StringToByteArray", "Conversion failed: ${e.message}", e)
        null
    }
}
