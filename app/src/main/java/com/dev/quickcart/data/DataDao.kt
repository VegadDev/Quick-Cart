package com.dev.quickcart.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.dev.quickcart.data.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProduct(product: Product)

    @Query("SELECT * FROM product_table")
    fun getAllProducts(): Flow<List<Product>>

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM product_table WHERE id = :productId")
    suspend fun getProductById(productId: Int): Product

}
