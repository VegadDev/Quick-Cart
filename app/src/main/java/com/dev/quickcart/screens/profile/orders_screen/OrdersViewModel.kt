package com.dev.quickcart.screens.profile.orders_screen

import androidx.lifecycle.ViewModel
import com.dev.quickcart.navigation.Navigator
import com.dev.quickcart.screens.login.intro.IntroInterActor
import com.dev.quickcart.screens.profile.address_screen.AddressUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel
@Inject
constructor(
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    val interActor = object : OrdersInterActor {

    }
}