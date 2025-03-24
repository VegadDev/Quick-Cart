package com.dev.quickcart.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.data.model.CartItem
import com.dev.quickcart.data.repository.NetworkRepository
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class CartViewModel
@Inject
constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val networkRepository: NetworkRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private var cartListener: ListenerRegistration? = null

    val interActor = object : CartInterActor {
        override fun onBackClick() {
            navigator.navigate(NavigationCommand.Back)
        }

        override fun onIncrementClick(productId: String) {
            incrementQuantity(productId)
        }

        override fun onDecrementClick(productId: String) {
            decrementQuantity(productId)
        }

        override fun onRemoveClick(productId: String) {
            removeProduct(productId)
        }

    }


    init {
        fetchCartItems1()
        //startCartListener()
    }


    private fun startCartListener() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            cartListener = firestore.collection("users").document(userId)
                .collection("cart")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        _uiState.value = _uiState.value.copy(error = e.message)
                        return@addSnapshotListener
                    }
                    val cartItems = snapshot?.documents?.mapNotNull { doc ->
                        CartItem(
                            productId = doc.id,
                            productName = doc.getString("productName") ?: "",
                            productPrice = doc.getDouble("productPrice") ?: 0.0,
                            quantity = doc.getLong("quantity")?.toInt() ?: 1,
                            productImage = doc.getBlob("productImage"),
                            productType = doc.getString("productType") ?: "",
                            productTypeValue = doc.get("productTypeValue")
                        )
                    } ?: emptyList()

                    // Calculate total price
                    val totalPrice = cartItems.sumOf { it.productPrice * it.quantity }
                    _uiState.value = _uiState.value.copy(cartItems = cartItems, totalPrice = totalPrice)
                }
        }
    }



    fun fetchCartItems1() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                _uiState.value = CartUiState(
                    error = "User not signed in",
                    cartCount = 0,
                    totalPrice = 0.0
                )
                return@launch
            }

            _uiState.value = CartUiState(isLoading = true)
            networkRepository.getCartItems(userId).collect { result ->
                if (result.isSuccess) {
                    val cartItems = result.getOrNull() ?: emptyList()
                    val totalCount = cartItems.sumOf { it.quantity }
                    val totalPrice = cartItems.sumOf { it.productPrice * it.quantity }
                    _uiState.value = CartUiState(
                        cartItems = cartItems,
                        cartCount = totalCount,
                        totalPrice = totalPrice,
                        isLoading = false
                    )
                } else {
                    _uiState.value = CartUiState(
                        error = result.exceptionOrNull()?.message,
                        cartCount = 0,
                        totalPrice = 0.0,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun incrementQuantity(productId: String) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch

            // Optimistic update: Increment quantity locally first
            val currentItems = _uiState.value.cartItems.toMutableList()
            val itemIndex = currentItems.indexOfFirst { it.productId == productId }
            if (itemIndex != -1) {
                val updatedItem = currentItems[itemIndex].copy(quantity = currentItems[itemIndex].quantity + 1)
                currentItems[itemIndex] = updatedItem
                updateUiState(currentItems)
            }

            // Set loading state
            _uiState.value = _uiState.value.copy(loadingItemsPlus = _uiState.value.loadingItemsPlus + (productId to true))
            try {
                val cartRef = firestore.collection("users").document(userId).collection("cart").document(productId)
                firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(cartRef)
                    if (snapshot.exists()) {
                        val currentQuantity = snapshot.getLong("quantity")?.toInt() ?: 1
                        transaction.update(cartRef, "quantity", currentQuantity + 1)
                    }
                }.await()
            } catch (e: Exception) {
                // Rollback on error
                val rolledBackItems = _uiState.value.cartItems.map {
                    if (it.productId == productId) it.copy(quantity = it.quantity - 1) else it
                }
                updateUiState(rolledBackItems)
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                // Clear loading state
                _uiState.value = _uiState.value.copy(loadingItemsPlus = _uiState.value.loadingItemsPlus - productId)
            }
        }
    }

    fun decrementQuantity(productId: String) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch

            // Optimistic update: Decrement quantity locally first
            val currentItems = _uiState.value.cartItems.toMutableList()
            val itemIndex = currentItems.indexOfFirst { it.productId == productId }
            if (itemIndex != -1) {
                val currentItem = currentItems[itemIndex]
                if (currentItem.quantity > 1) {
                    val updatedItem = currentItem.copy(quantity = currentItem.quantity - 1)
                    currentItems[itemIndex] = updatedItem
                } else {
                    currentItems.removeAt(itemIndex)
                }
                updateUiState(currentItems)
            }

            // Set loading state
            _uiState.value = _uiState.value.copy(loadingItemsMinus = _uiState.value.loadingItemsMinus + (productId to true))

            try {
                val cartRef = firestore.collection("users").document(userId).collection("cart").document(productId)
                firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(cartRef)
                    if (snapshot.exists()) {
                        val currentQuantity = snapshot.getLong("quantity")?.toInt() ?: 1
                        if (currentQuantity > 1) {
                            transaction.update(cartRef, "quantity", currentQuantity - 1)
                        } else {
                            transaction.delete(cartRef)
                        }
                    }
                }.await()
            } catch (e: Exception) {
                // Rollback on error
                val rolledBackItems = _uiState.value.cartItems.toMutableList()
                val rollbackIndex = rolledBackItems.indexOfFirst { it.productId == productId }
                if (rollbackIndex == -1) {
                    // Item was removed, add it back with quantity 1
                    val originalItem = currentItems.find { it.productId == productId }
                    if (originalItem != null) rolledBackItems.add(originalItem.copy(quantity = 1))
                } else {
                    rolledBackItems[rollbackIndex] = rolledBackItems[rollbackIndex].copy(quantity = rolledBackItems[rollbackIndex].quantity + 1)
                }
                updateUiState(rolledBackItems)
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                // Clear loading state
                _uiState.value = _uiState.value.copy(loadingItemsMinus = _uiState.value.loadingItemsMinus - productId)
            }
        }
    }

    fun removeProduct(productId: String) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch

            // Optimistic update: Remove item locally first
            val currentItems = _uiState.value.cartItems.filter { it.productId != productId }
            updateUiState(currentItems)

            _uiState.value = _uiState.value.copy(loadingItems = _uiState.value.loadingItems + (productId to true))
            try {
                val cartRef = firestore.collection("users").document(userId).collection("cart").document(productId)
                cartRef.delete().await()
            } catch (e: Exception) {
                // Rollback on error (re-add item)
                val originalItem = _uiState.value.cartItems.find { it.productId == productId }
                if (originalItem != null) {
                    updateUiState(_uiState.value.cartItems + originalItem)
                }
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(loadingItems = _uiState.value.loadingItems - productId)
            }
        }
    }

    // Helper function to update UI state with new cart items
    private fun updateUiState(cartItems: List<CartItem>) {
        val totalCount = cartItems.sumOf { it.quantity }
        val totalPrice = cartItems.sumOf { it.productPrice * it.quantity }
        _uiState.value = _uiState.value.copy(
            cartItems = cartItems,
            cartCount = totalCount,
            totalPrice = totalPrice
        )
    }

    override fun onCleared() {
        super.onCleared()
    }


}