package com.dev.quickcart.screens.productPage

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dev.quickcart.R
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.ui.theme.AppTheme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

@Composable
fun ProductPageScreen(interActor: ProductPageInterActor, uiState: ProductPageUiState) {


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
                    cardColor = AppTheme.colors.onSecondary,
                    cardCorner = 50,
                    onClick = { interActor.onBackClick() },
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                ) {
                    CustomIcon(
                        icon = R.drawable.ic_back_arrow,
                        modifier = Modifier
                            .padding(9.dp)
                            .padding(end = 2.dp),
                        imageModifier = Modifier.size(25.dp)
                    )
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, bottom = 50.dp)
                ) {


                    val imagePaths: List<String>? = Gson().fromJson(
                        uiState.productImage, object : TypeToken<List<String>>() {}.type
                    )
                    val firstImagePath = imagePaths?.firstOrNull() // Get the first image only

                    if (!firstImagePath.isNullOrEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(File(firstImagePath))  // Pass extracted file path
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(230.dp)
                                .align(Alignment.Center),
                            contentScale = ContentScale.Fit
                        )
                        Log.d("ProductDetails", "Image Path: ${uiState.productImage}")
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
                Text(
                    text = uiState.productName,
                    style = AppTheme.textStyles.extraBold.largeTitle,
                    color = AppTheme.colors.titleText
                )
                Text(
                    formatWeight(uiState.productTypeValue),
                    style = AppTheme.textStyles.bold.large,
                    color = AppTheme.colors.secondary
                )

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
                        uiState.productPrice.toIntOrNull()?.times(productQuality.value)

                    Spacer(Modifier.weight(1f))
                    Text(
                        "₹ ${updatedPrice}.0",
                        style = AppTheme.textStyles.extraBold.large,
                        color = AppTheme.colors.titleText,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(15.dp)
                    )

                }

                Text(
                    "Product Description : \n${uiState.prodDescription}",
                    style = AppTheme.textStyles.bold.large,
                    color = AppTheme.colors.lightGray,
                    modifier = Modifier.padding(top = 10.dp)
                )

                Text(
                    "One Apple gives :",
                    style = AppTheme.textStyles.extraBold.large,
                    color = AppTheme.colors.titleText,
                    modifier = Modifier.padding(top = 15.dp, bottom = 15.dp)
                )

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
                                Text(
                                    "${uiState.prodProtein} Gram",
                                    style = AppTheme.textStyles.bold.large,
                                    color = AppTheme.colors.primary,
                                    modifier = Modifier
                                )
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


fun formatWeight(grams: String): String {
    return if (grams >= 1000.toString()) {
        val kg = grams.toInt() / 1000.0 // Convert to kg
        String.format("%.2f kg", kg) // Format to 2 decimal places
    } else {
        "$grams g"
    }
}
