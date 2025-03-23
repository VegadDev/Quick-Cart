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
        fetchCartItems()
        //startCartListener()
    }

    fun fetchCartItems() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                _uiState.value = CartUiState(error = "User not signed in")
                uiState.value.cartCount = 0
                uiState.value.cartTotal = 0
                return@launch
            }

            _uiState.value = CartUiState(isLoading = true)
            networkRepository.getCartItems(userId).collect { result ->
                if (result.isSuccess) {
                    val cartItems = result.getOrNull() ?: emptyList()
                    val totalCount = cartItems.sumOf { it.quantity } // Calculate total count
                    _uiState.value = CartUiState(cartItems = cartItems)
                    uiState.value.cartCount = totalCount

//                    val totalAmount = cartItems.sumOf { it.productPrice * it.quantity }
//                    _uiState.value = CartUiState(cartTotal = totalAmount.toInt())
//                    uiState.value.cartTotal = totalAmount.toInt()

                } else {
                    _uiState.value = CartUiState(error = result.exceptionOrNull()?.message)
                    uiState.value.cartCount = 0
                    uiState.value.cartTotal = 0
                }
            }
        }
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
                            productName = doc.getString("name") ?: "",
                            productPrice = doc.getDouble("price") ?: 0.0,
                            quantity = doc.getLong("quantity")?.toInt() ?: 1,
                            productImage = doc.getBlob("image")
                        )
                    } ?: emptyList()
                    _uiState.value = _uiState.value.copy(cartItems = cartItems)
                }
        }
    }


    fun incrementQuantity(productId: String) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            _uiState.value = _uiState.value.copy(
                loadingItemsPlus = _uiState.value.loadingItemsPlus + (productId to true)
            )
            delay(300)
            try {
                val cartRef = firestore.collection("users").document(userId)
                    .collection("cart").document(productId)
                firestore.runTransaction { transaction ->
                    val snapshot = transaction.get(cartRef)
                    if (snapshot.exists()) {
                        val currentQuantity = snapshot.getLong("quantity")?.toInt() ?: 1
                        transaction.update(cartRef, "quantity", currentQuantity + 1)
                    }
                }.await()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(
                    loadingItemsPlus = _uiState.value.loadingItemsPlus - productId
                )
            }
        }
    }

    fun decrementQuantity(productId: String) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            _uiState.value = _uiState.value.copy(
                loadingItemsMinus = _uiState.value.loadingItemsMinus + (productId to true)
            )
            delay(300)
            try {
                val cartRef = firestore.collection("users").document(userId)
                    .collection("cart").document(productId)
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
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(
                    loadingItemsMinus = _uiState.value.loadingItemsMinus - productId
                )
            }
        }
    }

    fun removeProduct(productId: String) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            _uiState.value = _uiState.value.copy(
                loadingItemsMinus = _uiState.value.loadingItemsMinus + (productId to true)
            )
            try {
                val cartRef = firestore.collection("users").document(userId)
                    .collection("cart").document(productId)
                cartRef.delete().await()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(
                    loadingItemsMinus = _uiState.value.loadingItemsMinus - productId
                )
            }
        }
    }

    override fun onCleared() {
        cartListener?.remove()
        super.onCleared()
    }



}