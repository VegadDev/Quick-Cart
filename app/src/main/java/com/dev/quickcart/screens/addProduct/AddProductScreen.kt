package com.dev.quickcart.screens.addProduct

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dev.quickcart.screens.common.CustomTextField
import com.dev.quickcart.ui.theme.AppTheme

@Composable
fun AddProductScreen(interActor: AddProductInterActor , uiState: AddProductUiState) {


    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(20.dp)
    ) {


        Text("Product Name",
            style = AppTheme.textStyles.bold.large,
            color = AppTheme.colors.titleText
        )
        Spacer(Modifier.size(10.dp))
        CustomTextField(
            value = uiState.productName,
            onValueChange = { interActor.updateProdName(it)},
            onSearch = {}
        )


        Text("Product Images",
            style = AppTheme.textStyles.bold.large,
            color = AppTheme.colors.titleText
        )
        Spacer(Modifier.size(10.dp))
        CustomTextField(
            value = uiState.productName,
            onValueChange = { interActor.updateProdName(it)},
            onSearch = {}
        )

    }




}