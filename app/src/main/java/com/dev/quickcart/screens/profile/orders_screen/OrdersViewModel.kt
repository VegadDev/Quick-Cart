package com.dev.quickcart.screens.profile.orders_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.quickcart.data.model.Order
import com.dev.quickcart.data.repository.NetworkRepository
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel
@Inject
constructor(
    private val navigator: Navigator,
    private val networkRepository: NetworkRepository,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    init {
        fetchOrders()
    }

    val interActor = object : OrdersInterActor {
        override fun onBackClick() {
            navigator.navigate(NavigationCommand.Back)
        }

    }


    private fun fetchOrders() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            firestore.collection("users")
                .document(userId)
                .collection("orders")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        _uiState.value = _uiState.value.copy(error = error.message)
                        return@addSnapshotListener
                    }
                    val orders = snapshot?.documents?.mapNotNull { doc ->
                        try {
                            doc.toObject(Order::class.java)
                        } catch (e: Exception) {
                            Log.e("ProfileViewModel", "Deserialization error: ${e.message}")
                            null
                        }
                    } ?: emptyList()
                    _uiState.value = _uiState.value.copy(orders = orders)
                }
        }
    }


}