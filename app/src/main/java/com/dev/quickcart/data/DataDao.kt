package com.dev.quickcart.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.dev.quickcart.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {

    @Upsert()
    suspend fun upsertProduct(product: Product)

    @Query("SELECT * FROM product_table")
    fun getAllUsers(): Flow<List<Product>>

    @Delete
    suspend fun deleteUser(product: Product)

}
