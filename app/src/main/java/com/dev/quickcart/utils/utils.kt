package com.dev.quickcart.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import coil.Coil
import coil.request.ImageRequest
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

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



fun saveImageToInternalStorage(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.filesDir, "${System.currentTimeMillis()}.jpg")
    val outputStream = FileOutputStream(file)
    inputStream?.copyTo(outputStream)
    inputStream?.close()
    outputStream.close()
    return file.absolutePath // Return saved image path
}


