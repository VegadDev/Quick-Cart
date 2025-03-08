package com.dev.quickcart.data.repository

import com.dev.quickcart.data.DataDao
import com.dev.quickcart.data.model.Product
import com.dev.quickcart.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DataRepository {

    suspend fun upsertProduct(product: Product)
}



class DataRepositoryImpl
@Inject
constructor(
    private val dataDao: DataDao,
) : DataRepository {

    override suspend fun upsertProduct(product: Product){

        return dataDao.upsertProduct(product)

    }

}