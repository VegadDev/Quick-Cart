package com.dev.quickcart.screens.cart

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.data.model.CartItem
import com.dev.quickcart.data.model.Order
import com.dev.quickcart.data.repository.NetworkRepository
import com.dev.quickcart.navigation.AppScreens
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.text.toInt


@HiltViewModel
class CartViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val networkRepository: NetworkRepository,
    private val navigator: Navigator
) : ViewModel() {

    val addressId = savedStateHandle.get<String>("json")?.toString()
        ?: throw IllegalStateException("Address Id is null")

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()


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

        override fun proceedToCheckout() {
            proceedToCheckout1()
        }

    }


    init {
        fetchCartItems()
    }

    fun proceedToCheckout1() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val cartItems = _uiState.value.cartItems
            val selectedAddress = _uiState.value.selectedAddress
            val totalPrice = _uiState.value.totalPrice

            if (selectedAddress == null) {
                _uiState.value = _uiState.value.copy(error = "Address select karo pehle!")
                return@launch
            }
            if (cartItems.isEmpty()) {
                _uiState.value = _uiState.value.copy(error = "Cart khali hai!")
                return@launch
            }

            val order = Order(
                userId = userId,
                address = selectedAddress,
                cartItems = cartItems,
                totalPrice = totalPrice
            )

            val orderResult = networkRepository.placeOrder(order)
            if (orderResult.isSuccess) {
                val clearResult = networkRepository.clearCart(userId)
                if (clearResult.isSuccess) {
                    _uiState.value = CartUiState() // Reset UI state
                    navigator.navigate(NavigationCommand.ToAndClearAll(AppScreens.HomeScreen.route))
                } else {
                    _uiState.value = _uiState.value.copy(error = "Cart clear nahi hua!")
                }
            } else {
                _uiState.value = _uiState.value.copy(error = "Order place nahi hua!")
            }
        }
    }



    fun fetchCartItems() {
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
            combine(
                networkRepository.getCartItems(userId),
                networkRepository.getUserAddresses(userId)
            ) { cartResult, addressResult ->
                val cartItems = if (cartResult.isSuccess) cartResult.getOrNull() ?: emptyList() else emptyList()
                val addresses = if (addressResult.isSuccess) addressResult.getOrNull() ?: emptyList() else emptyList()

                val totalCount = cartItems.sumOf { it.quantity }
                val totalPrice = cartItems.sumOf { it.productPrice * it.quantity }
                val selectedCategory = networkRepository.getSelectedAddress().value
                val selectedAddress = if (selectedCategory != null) {
                    addresses.find { it.category == selectedCategory }
                } else null

                CartUiState(
                    cartItems = cartItems,
                    selectedAddress = selectedAddress,
                    cartCount = totalCount,
                    totalPrice = totalPrice,
                    isLoading = false,
                    error = if (cartResult.isFailure) cartResult.exceptionOrNull()?.message else null
                )
            }.collect { newState ->
                _uiState.value = newState
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