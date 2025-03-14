package com.dev.quickcart.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dev.quickcart.screens.common.MyButton

@Composable
fun AdminScreen(interActor: AdminInterActor, uiState: AdminUiState) {


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        MyButton(
            "Add Product",
            onClick = { interActor.gotoAddProduct() }
        )


        MyButton(
            "Logout",
            onClick = { interActor.logout() }
        )


    }



}