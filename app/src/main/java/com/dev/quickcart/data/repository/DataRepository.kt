package com.dev.quickcart.data.repository

import com.dev.quickcart.data.DataDao
import com.dev.quickcart.data.model.Product
import com.dev.quickcart.utils.DataState
import com.dev.quickcart.utils.ServerResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject
import com.dev.quickcart.utils.executeSafeFlow
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okio.ByteString.Companion.toByteString

interface DataRepository {

    suspend fun upsertProduct(product: Product): Flow<DataState<Product>>
    fun getAllProduct(): Flow<List<Product>>
    suspend fun getProductById(productPageId: Int): Product

    fun addToCart(product: Product)
    fun removeFromCart(product: Product)
    fun getCartItems(): Flow<List<Product>>

}


class DataRepositoryImpl
@Inject
constructor(
    private val dataDao: DataDao,
) : DataRepository {

    override suspend fun upsertProduct(product: Product): Flow<DataState<Product>> = flow {
        emit(DataState.loading())
        try {
            dataDao.upsertProduct(product)
            emit(DataState.success(product, "Product saved successfully"))
        } catch (e: Exception) {
            emit(DataState.failure(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)


    override fun getAllProduct(): Flow<List<Product>> =
        dataDao.getAllProducts().map { entities ->
            entities.map { entity ->
                Product(
                    id = entity.id,
                    prodName = entity.prodName,
                    prodImage = entity.prodImage,
                    prodPrice = entity.prodPrice,
                    prodDescription = entity.prodDescription,
                    lastModified = entity.lastModified
                )
            }

        }

    override suspend fun getProductById(productPageId: Int): Product {
        return dataDao.getProductById(productPageId)
    }

    // In-memory cart list (you can change this to persist in Room)
    private val _cartItems = MutableStateFlow<List<Product>>(emptyList())
    override fun getCartItems(): Flow<List<Product>> = _cartItems.asStateFlow()

    override fun addToCart(product: Product) {
        _cartItems.value = _cartItems.value + product
    }

    override fun removeFromCart(product: Product) {
        _cartItems.value = _cartItems.value - product
    }


}

