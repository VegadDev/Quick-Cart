package com.dev.quickcart.utils

import com.google.gson.annotations.SerializedName

data class DataState<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T?, msg: String? = null): DataState<T> {
            return DataState(Status.SUCCESS, data, msg)
        }

        fun <T> failure(msg: String): DataState<T> {
            return DataState(Status.FAILURE, null, msg)
        }

        fun <T> loading(): DataState<T> {
            return DataState(Status.LOADING, null, null)
        }

        fun <T> initial(data: T?): DataState<T> {
            return DataState(Status.INITIAL, data, null)
        }
    }
}

//data class LoginResponse(
//    @SerializedName("user") val user: UserDto,
//    @SerializedName("address") val library: List<LibraryDto> = emptyList(),
//)

enum class Status {
    SUCCESS,
    FAILURE,
    LOADING,
    INITIAL
}

data class ServerResponse<out T>(
    @SerializedName("statusCode") val statusCode: Int = 0,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: T? = null
) {
    val isSuccess: Boolean
        get() {
            return statusCode == 1
        }
}

data class UpsertData(
    @SerializedName("id") val id: Int,
    @SerializedName("lastModified") val lastModified: Long,
)
