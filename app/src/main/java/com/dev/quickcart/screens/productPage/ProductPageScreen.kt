package com.dev.quickcart.screens.productPage

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dev.quickcart.R
import com.dev.quickcart.screens.addProduct.stringToByteArray
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.ui.theme.AppTheme
import com.dev.quickcart.utils.displayQuantity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

@Composable
fun ProductPageScreen(interActor: ProductPageInterActor, uiState: ProductPageUiState) {


    if (uiState.isLoading){
        CircularProgressIndicator()
    }
    else {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(AppTheme.colors.cardBackgroundColor),
                    shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
                ) {

                    CustomCard(
                        cardColor = AppTheme.colors.onPrimary,
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
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(top = 10.dp, bottom = 50.dp)
                    ) {
                        if (true) {
                            val byteArray = uiState.products?.prodImage?.toBytes()
                            val bitmap = byteArray.let { it?.let { it1 -> BitmapFactory.decodeByteArray(it, 0, it1.size) } }
                            if (bitmap != null) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            } else {
                                androidx.compose.material3.Text("Failed to load image")
                            }
                            Log.d("ProductDetails", "Image Path: ${uiState.products?.prodImage}")
                        } else {
                            Text("Image not available", color = Color.Red)
                        }
                    }
                }

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(15.dp)
                ) {
                    uiState.products?.let {
                        Text(
                            text = it.prodName,
                            style = AppTheme.textStyles.extraBold.largeTitle,
                            color = AppTheme.colors.titleText,
                            modifier = Modifier.padding(top = 5.dp , start = 8.dp)
                        )
                    }
                    uiState.products?.let {
                        Text(
                            text = displayQuantity(it),
                            style = AppTheme.textStyles.bold.large,
                            color = AppTheme.colors.secondary,
                            modifier = Modifier.padding(top = 5.dp , start = 8.dp)
                        )
                    }

                    var productQuality = remember { mutableStateOf(1) }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        CustomIcon(
                            icon = R.drawable.ic_remove,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable {
                                    if (productQuality.value > 1) {
                                        productQuality.value = productQuality.value - 1
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
                                text = productQuality.value.toString(),
                                style = AppTheme.textStyles.bold.large,
                                color = AppTheme.colors.titleText,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                            )
                        }
                        CustomIcon(
                            icon = R.drawable.ic_add,
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 12.dp)
                                .clickable {
                                    productQuality.value = (productQuality.value.toInt() + 1)
                                },
                            imageModifier = Modifier.size(30.dp),
                            colorFilter = ColorFilter.tint(AppTheme.colors.primary),
                        )

                        var updatedPrice =
                            uiState.products?.prodPrice?.toIntOrNull()?.times(productQuality.value)

                        Spacer(Modifier.weight(1f))
                        Text(
                            "₹ ${updatedPrice}.0",
                            style = AppTheme.textStyles.extraBold.large,
                            color = AppTheme.colors.titleText,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(15.dp)
                        )

                    }

                    uiState.products?.let {
                        Text(
                            "Product Description : \n${it.prodDescription}",
                            style = AppTheme.textStyles.bold.large,
                            color = AppTheme.colors.lightGray,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }

                    Spacer(Modifier.size(7.dp))
                    uiState.products?.let {
                        if (it.productType == "weighed") {
                            Text(
                                "100 Grams of ${uiState.products.prodName} gives :",
                                style = AppTheme.textStyles.extraBold.large,
                                color = AppTheme.colors.titleText,
                                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
                            )
                        } else {
                            Text(
                                "One ${uiState.products.prodName}gives :",
                                style = AppTheme.textStyles.extraBold.large,
                                color = AppTheme.colors.titleText,
                                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
                            )
                        }
                    }

                    Row(Modifier.fillMaxWidth()) {
                        CustomCard(
                            cardColor = AppTheme.colors.background,
                            cardCorner = 20,
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, AppTheme.colors.borderGray),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                            ) {
                                CustomIcon(
                                    icon = R.drawable.ic_protein,
                                    modifier = Modifier.padding(start = 15.dp),
                                    imageModifier = Modifier.size(50.dp),
                                    isCircle = false
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .padding(vertical = 16.dp)
                                ) {
                                    uiState.products?.let {
                                        Text(
                                            "${it.prodProtein} Gram",
                                            style = AppTheme.textStyles.bold.large,
                                            color = AppTheme.colors.primary,
                                            modifier = Modifier
                                        )
                                    }
                                    Text(
                                        "Protein",
                                        style = AppTheme.textStyles.bold.large,
                                        color = AppTheme.colors.lightGray,
                                        modifier = Modifier.padding(top = 3.dp)
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.size(20.dp))
                        CustomCard(
                            cardColor = AppTheme.colors.background,
                            cardCorner = 20,
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, AppTheme.colors.borderGray),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                            ) {
                                CustomIcon(
                                    icon = R.drawable.ic_calories,
                                    modifier = Modifier.padding(start = 15.dp),
                                    imageModifier = Modifier.size(50.dp),
                                    isCircle = false
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .padding(vertical = 16.dp)
                                ) {
                                    Text(
                                        "${uiState.prodCalories} Gram",
                                        style = AppTheme.textStyles.bold.large,
                                        color = AppTheme.colors.primary,
                                        modifier = Modifier
                                    )
                                    Text(
                                        "Calories",
                                        style = AppTheme.textStyles.bold.large,
                                        color = AppTheme.colors.lightGray,
                                        modifier = Modifier.padding(top = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.size(20.dp))

                    Row(Modifier.fillMaxWidth()) {
                        CustomCard(
                            cardColor = AppTheme.colors.background,
                            cardCorner = 20,
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, AppTheme.colors.borderGray),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                            ) {
                                CustomIcon(
                                    icon = R.drawable.ic_fiber,
                                    modifier = Modifier.padding(start = 15.dp),
                                    imageModifier = Modifier.size(50.dp),
                                    isCircle = false
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .padding(vertical = 16.dp)
                                ) {
                                    Text(
                                        "${uiState.prodFiber} Gram",
                                        style = AppTheme.textStyles.bold.large,
                                        color = AppTheme.colors.primary,
                                        modifier = Modifier
                                    )
                                    Text(
                                        "Fiber",
                                        style = AppTheme.textStyles.bold.large,
                                        color = AppTheme.colors.lightGray,
                                        modifier = Modifier.padding(top = 3.dp)
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.size(20.dp))
                        CustomCard(
                            cardColor = AppTheme.colors.background,
                            cardCorner = 20,
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, AppTheme.colors.borderGray),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                            ) {
                                CustomIcon(
                                    icon = R.drawable.ic_fat,
                                    modifier = Modifier.padding(start = 15.dp),
                                    imageModifier = Modifier.size(50.dp),
                                    isCircle = false
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .padding(vertical = 16.dp)
                                ) {
                                    Text(
                                        "${uiState.prodFat} Gram",
                                        style = AppTheme.textStyles.bold.large,
                                        color = AppTheme.colors.primary,
                                        modifier = Modifier
                                    )
                                    Text(
                                        "Fat",
                                        style = AppTheme.textStyles.bold.large,
                                        color = AppTheme.colors.lightGray,
                                        modifier = Modifier.padding(top = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }

    }
}


