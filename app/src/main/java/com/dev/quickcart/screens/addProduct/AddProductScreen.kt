package com.dev.quickcart.screens.addProduct

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dev.quickcart.screens.common.CustomTextField
import com.dev.quickcart.screens.common.ImagePicker
import com.dev.quickcart.screens.common.MyButton
import com.dev.quickcart.screens.common.MyDropDown1
import com.dev.quickcart.ui.theme.AppTheme

@Composable
fun AddProductScreen(interActor: AddProductInterActor, uiState: AddProductUiState) {


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        item {

            Text(
                "Product Name",
                style = AppTheme.textStyles.bold.large,
                color = AppTheme.colors.titleText
            )
            Spacer(Modifier.size(10.dp))
            CustomTextField(
                value = uiState.productName,
                onValueChange = { interActor.updateProdName(it) },
                onSearch = {},
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                error = uiState.productNameError,
                hint = "Type Here"
            )
            Spacer(Modifier.size(20.dp))


            Text(
                "Product Images",
                style = AppTheme.textStyles.bold.large,
                color = AppTheme.colors.titleText
            )
            Spacer(Modifier.size(10.dp))


            ImagePicker(
                onImagePicked = { uri ->
                    if (uri != null) {
                        uiState.productImage = uri
                    }
                }
            )

            Log.d("Image:", "${uiState.productImage}")

            Spacer(Modifier.size(20.dp))

            Text(
                "Product Price",
                style = AppTheme.textStyles.bold.large,
                color = AppTheme.colors.titleText
            )
            Spacer(Modifier.size(10.dp))
            CustomTextField(
                value = uiState.productPrice,
                onValueChange = { interActor.updateProdPrice(it) },
                onSearch = {},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                error = uiState.productPriceError,
                hint = "Type Here"
            )
            Spacer(Modifier.size(20.dp))

            Text(
                "Product Description",
                style = AppTheme.textStyles.bold.large,
                color = AppTheme.colors.titleText
            )
            Spacer(Modifier.size(10.dp))
            CustomTextField(
                value = uiState.productDescription,
                onValueChange = { interActor.updateProdDescription(it) },
                onSearch = {},
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                error = uiState.productDescriptionError,
                hint = "Type Here"
            )

            Spacer(Modifier.size(20.dp))

            Text(
                "Product Category",
                style = AppTheme.textStyles.bold.large,
                color = AppTheme.colors.titleText
            )
            Spacer(Modifier.size(10.dp))
            MyDropDown1(
                items = listOf("Fruits", "Vegetable"),
                onItemSelected = { interActor.updateProdCategory(it)},
                title = "Select Category",
                corner = 15,
            )

            Spacer(Modifier.size(20.dp))

            Text(
                "Product Type",
                style = AppTheme.textStyles.bold.large,
                color = AppTheme.colors.titleText
            )
            Spacer(Modifier.size(10.dp))
            MyDropDown1(
                items = listOf("counted", "weighed"),
                onItemSelected = { interActor.updateProdType(it) },
                title = "Select Product Type",
                corner = 15,
            )

            Spacer(Modifier.size(10.dp))
            CustomTextField(
                value = uiState.productTypeValue,
                onValueChange = { interActor.updateProdTypeValue(it) },
                onSearch = {},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                error = uiState.productTypeValueError,
                hint = "Type Here"
            )

            Spacer(Modifier.size(20.dp))
            Text(
                "Product Protein",
                style = AppTheme.textStyles.bold.large,
                color = AppTheme.colors.titleText
            )
            Spacer(Modifier.size(10.dp))
            CustomTextField(
                value = uiState.productProtein,
                onValueChange = { interActor.updateProdProtein(it) },
                onSearch = {},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                error = uiState.productProteinError,
                hint = "Type Here"
            )


            Spacer(Modifier.size(20.dp))
            Text(
                "Product Calories",
                style = AppTheme.textStyles.bold.large,
                color = AppTheme.colors.titleText
            )
            Spacer(Modifier.size(10.dp))
            CustomTextField(
                value = uiState.productCalories,
                onValueChange = { interActor.updateProdCalories(it) },
                onSearch = {},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                error = uiState.productCaloriesError,
                hint = "Type Here"
            )

            Spacer(Modifier.size(20.dp))
            Text(
                "Product Fat",
                style = AppTheme.textStyles.bold.large,
                color = AppTheme.colors.titleText
            )
            Spacer(Modifier.size(10.dp))
            CustomTextField(
                value = uiState.productFat,
                onValueChange = { interActor.updateProdFat(it) },
                onSearch = {},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                error = uiState.productFatError,
                hint = "Type Here"
            )

            Spacer(Modifier.size(20.dp))
            Text(
                "Product Fiber",
                style = AppTheme.textStyles.bold.large,
                color = AppTheme.colors.titleText
            )
            Spacer(Modifier.size(10.dp))
            CustomTextField(
                value = uiState.productFiber,
                onValueChange = { interActor.updateProdFiber(it) },
                onSearch = {},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                error = uiState.productFiberError,
                hint = "Type Here"
            )


            Spacer(Modifier.size(30.dp))
            MyButton(
                "Submit",
                onClick = {
                    interActor.submit()
                }
            )

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            uiState.successMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            uiState.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }


        }
    }
}










