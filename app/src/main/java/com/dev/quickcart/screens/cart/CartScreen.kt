package com.dev.quickcart.screens.cart

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.dev.quickcart.ui.theme.AppTheme

@Composable
fun CartScreen(interActor: CartInterActor , uiState: CartUiState) {


    LazyColumn {
        items(uiState.cartItems){item->
            Text(item.productName,
                color = AppTheme.colors.titleText
            )

        }
    }

}