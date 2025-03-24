package com.dev.quickcart.screens.productPage

import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.quickcart.R
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.MyButton
import com.dev.quickcart.ui.theme.AppTheme
import com.dev.quickcart.utils.displayQuantity

@Composable
fun ProductPageScreen(interActor: ProductPageInterActor, uiState: ProductPageUiState) {

    val viewModel: ProductPageViewModel = hiltViewModel()
    val isAdding = viewModel.isAdding.collectAsState().value

    val productQuantity = remember { mutableStateOf(1) }


    if (uiState.isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Allow space for the button at the bottom
                    .fillMaxWidth()
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(AppTheme.colors.cardBackgroundColor),
                        shape = RoundedCornerShape(topStart = 25.dp, bottomEnd = 25.dp)
                    ) {
                        // Back Button
                        CustomCard(
                            cardColor = AppTheme.colors.cardBackgroundColor,
                            cardCorner = 50,
                            onClick = { interActor.onBackClick() },
                            modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                        ) {
                            CustomIcon(
                                icon = R.drawable.ic_back_arrow,
                                modifier = Modifier
                                    .padding(9.dp)
                                    .padding(end = 2.dp),
                                imageModifier = Modifier.size(25.dp),
                                colorFilter = ColorFilter.tint(AppTheme.colors.textColorDark)
                            )
                        }

                        // Product Image
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .padding(top = 10.dp, bottom = 50.dp)
                        ) {
                            uiState.products?.prodImage?.let { blob ->
                                val bitmap = remember(blob) { // Cache bitmap decoding
                                    BitmapFactory.decodeByteArray(blob.toBytes(), 0, blob.toBytes().size)
                                }
                                if (bitmap != null) {
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Text("Failed to load image", color = Color.Red)
                                }
                            } ?: Text("Image not available", color = Color.Red)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        // Product Name
                        uiState.products?.let {
                            Text(
                                text = it.prodName,
                                style = AppTheme.textStyles.extraBold.largeTitle,
                                color = AppTheme.colors.titleText,
                                modifier = Modifier.padding(top = 5.dp, start = 8.dp)
                            )
                        }

                        // Quantity Display
                        uiState.products?.let {
                            Text(
                                text = displayQuantity(it),
                                style = AppTheme.textStyles.bold.large,
                                color = AppTheme.colors.secondary,
                                modifier = Modifier.padding(top = 5.dp, start = 8.dp)
                            )
                        }

                        // Quantity Selector

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomIcon(
                                icon = R.drawable.ic_remove,
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .clickable {
                                        if (productQuantity.value > 1) {
                                            productQuantity.value -= 1
                                        }
                                    },
                                imageModifier = Modifier.size(30.dp),
                                colorFilter = ColorFilter.tint(AppTheme.colors.minusGray)
                            )
                            CustomCard(
                                border = BorderStroke(1.dp, AppTheme.colors.borderGray),
                                cardCorner = 15,
                                isClickable = false
                            ) {
                                Text(
                                    text = productQuantity.value.toString(),
                                    style = AppTheme.textStyles.bold.large,
                                    color = AppTheme.colors.titleText,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                                )
                            }
                            CustomIcon(
                                icon = R.drawable.ic_add,
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 12.dp)
                                    .clickable { productQuantity.value += 1 },
                                imageModifier = Modifier.size(30.dp),
                                colorFilter = ColorFilter.tint(AppTheme.colors.primary)
                            )

                            // Updated Price
                            val updatedPrice = uiState.products?.let { product ->
                                product.prodPrice.toDoubleOrNull()?.times(productQuantity.value) ?: 0.0
                            } ?: 0.0
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "₹ $updatedPrice",
                                style = AppTheme.textStyles.extraBold.large,
                                color = AppTheme.colors.titleText,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(15.dp)
                            )
                        }

                        // Product Description
                        uiState.products?.let {
                            Text(
                                text = "Product Description: \n${it.prodDescription}",
                                style = AppTheme.textStyles.bold.large,
                                color = AppTheme.colors.lightGray,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.size(7.dp))

                        // Nutritional Info Header
                        uiState.products?.let {
                            Text(
                                text = if (it.productType == "weighed") {
                                    "100 Grams of ${it.prodName} gives:"
                                } else {
                                    "One ${it.prodName} gives:"
                                },
                                style = AppTheme.textStyles.extraBold.large,
                                color = AppTheme.colors.titleText,
                                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
                            )
                        }

                        // Nutritional Info Cards
                        Row(modifier = Modifier.fillMaxWidth()) {
                            NutritionalCard(
                                icon = R.drawable.ic_protein,
                                value = uiState.products?.prodProtein ?: "0",
                                label = "Protein",
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.size(20.dp))
                            NutritionalCard(
                                icon = R.drawable.ic_calories,
                                value = uiState.products?.prodCalories ?: "0",
                                label = "Calories",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.size(20.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            NutritionalCard(
                                icon = R.drawable.ic_fiber,
                                value = uiState.products?.prodFiber ?: "0",
                                label = "Fiber",
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.size(20.dp))
                            NutritionalCard(
                                icon = R.drawable.ic_fat,
                                value = uiState.products?.prodFat ?: "0",
                                label = "Fat",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Add to Cart Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (isAdding) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(36.dp)
                    )
                } else {
                    MyButton(
                        text = "Add to Cart",
                        onClick = {
                            uiState.products?.let { product ->
                                interActor.onAddToCartClick(
                                    product.copy(
                                        prodPrice = (product.prodPrice.toInt() * productQuantity.value).toString() // Adjust price based on quantity
                                    ),
                                    productQuantity.value
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// Helper Composable for Nutritional Info
@Composable
fun NutritionalCard(
    icon: Int,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    CustomCard(
        cardColor = AppTheme.colors.background,
        cardCorner = 20,
        modifier = modifier,
        border = BorderStroke(1.dp, AppTheme.colors.borderGray)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(15.dp)
        ) {
            CustomIcon(
                icon = icon,
                modifier = Modifier.padding(end = 10.dp),
                imageModifier = Modifier.size(43.dp),
                isCircle = false
            )
            Column {
                Text(
                    text = "$value Gram",
                    style = AppTheme.textStyles.bold.large,
                    color = AppTheme.colors.primary
                )
                Text(
                    text = label,
                    style = AppTheme.textStyles.bold.large,
                    color = AppTheme.colors.lightGray,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        }
    }
}

