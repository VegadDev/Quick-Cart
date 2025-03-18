package com.dev.quickcart.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log

import com.dev.quickcart.data.model.Product
import com.dev.quickcart.utils.uriToBlob

import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface NetworkRepository {

    suspend fun addProduct(product: Product, imageUri: Uri?): Result<String>

    suspend fun getProduct(productId: String): Result<Product>
    suspend fun getAllProducts(): Result<List<Product>>


    suspend fun getProducts(productId: String): Product?


}


class NetworkRepositoryImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : NetworkRepository {

    private val productsCollection = firestore.collection("products")
    private val counterRef = firestore.collection("counters").document("productCounter")


    override suspend fun addProduct(product: Product, imageUri: Uri?): Result<String> {
        return try {
            // Convert URI to Blob if provided
            val imageBlob = imageUri?.let { uriToBlob(context, it) }
            val productWithImage = product.copy(prodImage = imageBlob)

            // Run a transaction to get and increment the counter
            val newProdId = firestore.runTransaction { transaction ->
                val snapshot = transaction.get(counterRef)
                val currentId = snapshot.getLong("lastId")?.toInt() ?: 0
                val nextId = currentId + 1
                transaction.set(counterRef, mapOf("lastId" to nextId))
                nextId
            }.await()

            // Create document ID with the incremented prodId
            val documentId = "$newProdId"
            val productWithId = productWithImage.copy(prodId = newProdId)

            // Insert the product
            productsCollection.document(documentId).set(productWithId).await()
            Result.success(documentId)
        } catch (e: Exception) {
            Log.e("FirestoreDebug", "Write failed: ${e.message}", e)
            Result.failure(e)
        }
    }



    override suspend fun getProduct(productId: String): Result<Product> {
        return try {
            val document = productsCollection.document(productId).get().await()
            document.toObject(Product::class.java)?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Product not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val query = productsCollection.get().await()
            val products = query.toObjects(Product::class.java)
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProducts(productId: String): Product? {
        return try {
            val document = productsCollection.document(productId).get().await()

            // Check if the document exists and convert it to a Product object
            if (document.exists()) {
                document.toObject(Product::class.java)
            } else {
                null // Return null if the document doesn't exist
            }
        } catch (e: Exception) {
            // Log the error for debugging and return null
            Log.e("FirestoreDebug", "Failed to get product: ${e.message}", e)
            null
        }
    }


}


