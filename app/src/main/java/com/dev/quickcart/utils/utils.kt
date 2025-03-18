package com.dev.quickcart.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.dev.quickcart.data.model.Product
import com.google.firebase.firestore.Blob
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

fun <T, R> (suspend () -> ServerResponse<T>).executeSafeFlow(
    block: suspend (T) -> R
): Flow<DataState<R>> = flow<DataState<R>> {
    try {
        emit(DataState.loading())
        val response = this@executeSafeFlow.invoke()
        if (response.isSuccess) {
            val result = block(response.data!!)
            emit(DataState.success(result, response.message))
        } else {
            emit(DataState.failure(response.message ?: "Something went wrong"))
        }
    } catch (e: Exception) {
        emit(DataState.failure(e.message ?: "Something went wrong"))
    }
}.flowOn(Dispatchers.IO)


fun Any.toJsonObject(): JsonObject {
    val jsonElement: JsonElement = Gson().toJsonTree(this)
    return jsonElement.asJsonObject
}


internal suspend fun uriToBlob(context: Context, uri: Uri): Blob? {
    return withContext(Dispatchers.IO) {
        try {
            val byteArray = uriToByteArray(context, uri)
            byteArray?.let { Blob.fromBytes(it) }
        } catch (e: Exception) {
            Log.e("ImageDebug", "Failed to convert URI to Blob: ${e.message}", e)
            null
        }
    }
}

private suspend fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            val byteArray = outputStream.toByteArray()
            inputStream?.close()
            outputStream.close()
            byteArray
        } catch (e: Exception) {
            Log.e("ImageDebug", "Failed to read image: ${e.message}", e)
            null
        }
    }
}


fun displayQuantity(product: Product): String {
    return when (product.productType) {
        "counted" -> {
            // For counted products, use the countedQuantity and append " piece"
            val quantity = product.productTypeValue ?: "0"
            "$quantity piece"
        }
        "weighed" -> {
            // For weighted products, check the weight
            val weight = product.productTypeValue.toInt()
            if (weight >= 1000) {
                // If weight > 1000 grams, convert to kilograms
                val kg = weight / 1000
                "$kg kg/price"
            } else {
                // If weight <= 1000 grams, show in grams
                "$weight g/price"
            }
        }
        else -> "Unknown product type"
    }
}