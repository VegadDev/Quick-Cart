package com.dev.quickcart.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.dev.quickcart.data.model.CartItem
import com.dev.quickcart.data.model.Order
import com.dev.quickcart.data.model.Product
import com.dev.quickcart.data.model.UserAddress
import com.dev.quickcart.utils.uriToBlob
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface NetworkRepository {

    suspend fun addProduct(product: Product, imageUri: Uri?): Result<String>

    suspend fun getAllProducts(): Result<List<Product>>

    suspend fun getProduct(productId: String): Result<Product?>

    suspend fun addOrUpdateCartItem(userId: String, cartItem: CartItem): Result<Unit>

    suspend fun getCartItems(userId: String): Flow<Result<List<CartItem>>>

    suspend fun updateCartItemQuantity(userId: String, productId: String, newQuantity: Int): Result<Unit>

    fun listenToCartItems(userId: String, onUpdate: (List<CartItem>) -> Unit): ListenerRegistration

    fun getUserAddresses(userId: String): Flow<Result<List<UserAddress>>>

    fun setSelectedAddress(category: String)

    fun getSelectedAddress(): StateFlow<String?>

    suspend fun placeOrder(order: Order): Result<Unit>

    suspend fun clearCart(userId: String): Result<Unit>
}

class NetworkRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : NetworkRepository {

    private val productsCollection = firestore.collection("products")
    private val counterRef = firestore.collection("counters").document("productCounter")
    private val _selectedAddress = MutableStateFlow<String?>(null)

    override suspend fun addProduct(product: Product, imageUri: Uri?): Result<String> {
        return try {
            val imageBlob = imageUri?.let { uriToBlob(context, it) }
            val productWithImage = product.copy(prodImage = imageBlob)

            val newProdId = firestore.runTransaction { transaction ->
                val snapshot = transaction.get(counterRef)
                val currentId = snapshot.getLong("lastId")?.toInt() ?: 0
                val nextId = currentId + 1
                transaction.set(counterRef, mapOf("lastId" to nextId))
                nextId
            }.await()

            val documentId = newProdId.toString()
            val productWithId = productWithImage.copy(prodId = newProdId)
            productsCollection.document(documentId).set(productWithId).await()
            Result.success(documentId)
        } catch (e: Exception) {
            Log.e("NetworkRepository", "Failed to add product: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val query = productsCollection.get().await()
            val products = query.toObjects(Product::class.java)
            Result.success(products)
        } catch (e: Exception) {
            Log.e("NetworkRepository", "Failed to get all products: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getProduct(productId: String): Result<Product?> {
        return try {
            val document = productsCollection.document(productId).get().await()
            val product = document.toObject(Product::class.java)
            Result.success(product)
        } catch (e: Exception) {
            Log.e("NetworkRepository", "Failed to get product: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun addOrUpdateCartItem(userId: String, cartItem: CartItem): Result<Unit> {
        return try {
            val cartRef = getCartRef(userId).document(cartItem.productId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(cartRef)
                if (snapshot.exists()) {
                    val currentQuantity = snapshot.getLong("quantity")?.toInt() ?: 1
                    transaction.update(cartRef, "quantity", currentQuantity + 1)
                } else {
                    transaction.set(cartRef, cartItem)
                }
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("NetworkRepository", "Failed to add/update cart item: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getCartItems(userId: String): Flow<Result<List<CartItem>>> = callbackFlow {
        val listenerRegistration = getCartRef(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.failure(error))
                return@addSnapshotListener
            }
            val cartItems = snapshot?.toObjects(CartItem::class.java) ?: emptyList()
            trySend(Result.success(cartItems))
        }
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun updateCartItemQuantity(userId: String, productId: String, newQuantity: Int): Result<Unit> {
        return try {
            getCartRef(userId).document(productId).update("quantity", newQuantity).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("NetworkRepository", "Failed to update cart item quantity: ${e.message}", e)
            Result.failure(e)
        }
    }

    override fun listenToCartItems(userId: String, onUpdate: (List<CartItem>) -> Unit): ListenerRegistration {
        return getCartRef(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("NetworkRepository", "Error listening to cart: ${error.message}", error)
                return@addSnapshotListener
            }
            val cartItems = snapshot?.toObjects(CartItem::class.java) ?: emptyList()
            onUpdate(cartItems)
        }
    }

    override fun getUserAddresses(userId: String): Flow<Result<List<UserAddress>>> = callbackFlow {
        if (userId.isBlank()) {
            trySend(Result.failure(IllegalArgumentException("User ID cannot be blank")))
            close()
            return@callbackFlow
        }
        val addressesRef = getAddressesRef(userId)
        val listener = addressesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.failure(error))
                return@addSnapshotListener
            }
            val addresses = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(UserAddress::class.java)?.copy(category = doc.id)
            } ?: emptyList()
            trySend(Result.success(addresses))
        }
        awaitClose { listener.remove() }
    }

    override fun setSelectedAddress(category: String) {
        _selectedAddress.value = category
    }

    override fun getSelectedAddress(): StateFlow<String?> = _selectedAddress.asStateFlow()

    override suspend fun placeOrder(order: Order): Result<Unit> {
        return try {
            getOrdersRef(order.userId).add(order).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("NetworkRepository", "Failed to place order: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun clearCart(userId: String): Result<Unit> {
        return try {
            val cartRef = getCartRef(userId)
            val snapshot = cartRef.get().await()
            val batch = firestore.batch()
            snapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("NetworkRepository", "Failed to clear cart: ${e.message}", e)
            Result.failure(e)
        }
    }

    // Helper functions
    private fun getCartRef(userId: String) = firestore.collection("users").document(userId).collection("cart")
    private fun getAddressesRef(userId: String) = firestore.collection("users").document(userId).collection("addresses")
    private fun getOrdersRef(userId: String) = firestore.collection("users").document(userId).collection("orders")
}