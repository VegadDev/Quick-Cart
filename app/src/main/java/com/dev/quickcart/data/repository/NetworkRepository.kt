package com.dev.quickcart.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.dev.quickcart.data.model.CartItem

import com.dev.quickcart.data.model.Product
import com.dev.quickcart.data.model.UserAddress
import com.dev.quickcart.utils.uriToBlob

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface NetworkRepository {

    suspend fun addProduct(product: Product, imageUri: Uri?): Result<String>

    suspend fun getAllProducts(): Result<List<Product>>

    suspend fun getProducts(productId: String): Product?

    fun getUserAddresses(userId: String): Flow<Result<List<UserAddress>>>

    suspend fun addOrUpdateCartItem(userId: String, cartItem: CartItem): Result<Unit>

    suspend fun getCartItems(userId: String): Flow<Result<List<CartItem>>>

    suspend fun updateCartItemQuantity(userId: String, productId: String, newQuantity: Int): Result<Unit>

    fun listenToCartItems(userId: String, onUpdate: (List<CartItem>) -> Unit): ListenerRegistration

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

    override fun getUserAddresses(userId: String): Flow<Result<List<UserAddress>>> = callbackFlow {
        // Ensure userId is valid
        if (userId.isBlank()) {
            trySend(Result.failure(IllegalArgumentException("User ID cannot be blank")))
            close()
            return@callbackFlow
        }

        val addressesRef = firestore.collection("users")
            .document(userId)
            .collection("addresses")

        // Real-time listener
        val listener = addressesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.failure(error))
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val addresses = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject(UserAddress::class.java)?.copy(category = doc.id)
                    } catch (e: Exception) {
                        null // Skip malformed documents
                    }
                }
                trySend(Result.success(addresses))
            } else {
                trySend(Result.success(emptyList())) // No data found
            }
        }

        // Cleanup listener when Flow is cancelled
        awaitClose { listener.remove() }
    }.catch { e ->
        emit(Result.failure(e)) // Handle any upstream errors
    }

    override suspend fun addOrUpdateCartItem(userId: String, cartItem: CartItem): Result<Unit> {
        return try {
            val cartRef = firestore.collection("users")
                .document(userId)
                .collection("cart")
                .document(cartItem.productId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(cartRef)
                if (snapshot.exists()) {
                    // Item exists, increment quantity
                    val currentQuantity = snapshot.getLong("quantity")?.toInt() ?: 1
                    transaction.update(cartRef, "quantity", currentQuantity + 1)
                } else {
                    // Item doesn’t exist, add it with quantity 1
                    transaction.set(cartRef, cartItem)
                }
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            println("Error adding/updating cart item: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getCartItems(userId: String): Flow<Result<List<CartItem>>> = callbackFlow {
        val listenerRegistration = firestore.collection("users")
            .document(userId)
            .collection("cart")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val cartItems = snapshot.toObjects(CartItem::class.java)
                    trySend(Result.success(cartItems))
                }
            }
        awaitClose { listenerRegistration.remove() }
    }


    override suspend fun updateCartItemQuantity(userId: String, productId: String, newQuantity: Int): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .collection("cart")
                .document(productId)
                .update("quantity", newQuantity)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun listenToCartItems(userId: String, onUpdate: (List<CartItem>) -> Unit): ListenerRegistration {
        val cartRef = firestore.collection("users")
            .document(userId)
            .collection("cart")
        return cartRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                println("Error listening to cart: ${error.message}")
                return@addSnapshotListener
            }
            snapshot?.let {
                val cartItems = it.toObjects(CartItem::class.java)
                onUpdate(cartItems)
            }
        }
    }

}


