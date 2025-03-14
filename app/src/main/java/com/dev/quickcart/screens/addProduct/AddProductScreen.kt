package com.dev.quickcart.screens.addProduct

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dev.quickcart.screens.common.CustomTextField
import com.dev.quickcart.screens.common.MyButton
import com.dev.quickcart.screens.common.MyDropDown
import com.dev.quickcart.ui.theme.AppTheme
import com.dev.quickcart.utils.saveImageToInternalStorage
import java.io.File
import java.io.FileOutputStream
import java.nio.file.WatchEvent

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

            var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
            val context = LocalContext.current

            val photoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickMultipleVisualMedia(),
                onResult = { uris -> selectedImageUris = uris }
            )
            MyButton(
                "Select Images",
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                items(selectedImageUris) { image ->
                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
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
            MyDropDown(
                items = listOf("Fruits", "Vegetable"),
                title = "Select Category",
                isicon = false,
                corner = 15,
            )

            Spacer(Modifier.size(20.dp))

            Text(
                "Product Type",
                style = AppTheme.textStyles.bold.large,
                color = AppTheme.colors.titleText
            )
            Spacer(Modifier.size(10.dp))
            MyDropDown(
                items = listOf("counted", "weighed"),
                //onItemSelected = { interActor.updateProdType(it) },
                title = "Select Product Type",
                isicon = false,
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
                    val imagePaths = selectedImageUris.map { uri ->
                        saveImageToInternalStorage(context, uri) // Convert URI to file path
                    }
                    uiState.productImage = imagePaths
                    interActor.submit()
                }
            )


        }
    }
}










