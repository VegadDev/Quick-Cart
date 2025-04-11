package com.dev.quickcart.screens.cart

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.quickcart.R
import com.dev.quickcart.data.model.CartItem
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.DashedLine
import com.dev.quickcart.screens.common.MyButton
import com.dev.quickcart.screens.common.SquareBorderProgressIndicator
import com.dev.quickcart.ui.theme.AppTheme
import com.dev.quickcart.utils.displayQuantity
import kotlinx.coroutines.launch

@Composable
fun CartScreen(interActor: CartInterActor, uiState: CartUiState) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.totalPrice == 0.0){
            Column(
                Modifier
                    .fillMaxSize()
                    .background(AppTheme.colors.background),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 120.dp),
            ) {

                CustomIcon(
                    icon = R.drawable.ic_back_arrow,
                    modifier = Modifier
                        .padding(start = 16.dp , top = 15.dp)
                        .clickable { interActor.onBackClick() },
                    imageModifier = Modifier.size(25.dp),
                    colorFilter = ColorFilter.tint(AppTheme.colors.onPrimary)
                )


                Text(
                    "My Cart 🛒",
                    style = AppTheme.textStyles.bold.large,
                    color = AppTheme.colors.titleText,
                    modifier = Modifier.padding(top = 15.dp)
                )

            }
            Divider(
                color = AppTheme.colors.lightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 20.dp)
            )
            CustomIcon(
                icon = R.drawable.ic_emptycart,
                modifier = Modifier.weight(0.8f),
                imageModifier = Modifier.size(250.dp),
            )
            Text(
                "Your cart is empty 😒",
                style = AppTheme.textStyles.bold.largeTitle,
                color = AppTheme.colors.titleText,
                modifier = Modifier.padding(top = 15.dp).weight(0.9f)
            )
            }
        }
        else {
            BottomSheetExample(
                interactor = interActor,
                uiState = uiState,
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(space = 120.dp),
                    ) {

                        CustomIcon(
                            icon = R.drawable.ic_back_arrow,
                            modifier = Modifier
                                .padding(start = 16.dp, top = 15.dp)
                                .clickable { interActor.onBackClick() },
                            imageModifier = Modifier.size(25.dp),
                            colorFilter = ColorFilter.tint(AppTheme.colors.onPrimary)
                        )


                        Text(
                            "My Cart 🛒",
                            style = AppTheme.textStyles.bold.largeTitle,
                            color = AppTheme.colors.titleText,
                            modifier = Modifier.padding(top = 15.dp)
                        )

                    }
                    Divider(
                        color = AppTheme.colors.lightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 20.dp)
                    )

                    uiState.selectedAddress?.let { address ->
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Shipped to: ${address.category}",
                                )
                                Text("Phone: ${address.phoneNumber}")

                            }
                    } ?: Text("No address selected")

                    LazyColumn(
                        modifier = Modifier.padding(top = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = uiState.cartItems,
                            key = { item -> item.productId }
                        ) { item ->
                            val isLoadingMinus = uiState.loadingItemsMinus[item.productId] == true
                            val isLoadingPlus = uiState.loadingItemsPlus[item.productId] == true
                            CartItemCard(
                                cartItem = item,
                                isLoadingMinus = isLoadingMinus,
                                isLoadingPlus = isLoadingPlus,
                                onRemove = { interActor.onRemoveClick(item.productId) },
                                onIncrement = { interActor.onIncrementClick(item.productId) },
                                onDecrement = { interActor.onDecrementClick(item.productId) }
                            )
                        }
                    }


                }


            }

            CustomCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                cardColor = AppTheme.colors.billCardBg
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DashedLine(
                        color = AppTheme.colors.divider,
                        thickness = 7f,
                        dashWidth = 40f,
                        dashGap = 20f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Grand total",
                            style = AppTheme.textStyles.extraBold.largeTitle,
                            color = AppTheme.colors.titleText,
                            fontSize = 19.sp
                        )
                        Spacer(Modifier.weight(1f))
                        val total = uiState.totalPrice + 40 + 15
                        Text(
                            "₹ $total",
                            style = AppTheme.textStyles.regular.large,
                            color = AppTheme.colors.titleText
                        )
                    }
                    MyButton(
                        "Check Out",
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(bottom = 15.dp),
                        onClick = { interActor.proceedToCheckout() }
                    )
                }
            }
        }
    }
}


@Composable
fun BottomSheetExample(
    modifier: Modifier = Modifier,
    interactor: CartInterActor,
    uiState: CartUiState,
    sheetContent: @Composable (() -> Unit) = {},
) {
    // Scaffold state to manage the bottom sheet's collapsed/expanded states
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    // Coroutine scope to programmatically expand/collapse the sheet
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetBackgroundColor = AppTheme.colors.billCardBg,
        sheetElevation = 20.dp,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        // Peek height ensures only the collapsed content is visible initially
        sheetPeekHeight = 179.dp, // Adjust this value based on your collapsed content height
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Show detailed breakdown only when the sheet is expanded
                if (scaffoldState.bottomSheetState.isExpanded) {
                    // Detailed breakdown section
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "📋  Items total",
                            style = AppTheme.textStyles.light.regular,
                            color = AppTheme.colors.billCardText,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            "₹ ${uiState.totalPrice}",
                            style = AppTheme.textStyles.regular.large,
                            color = AppTheme.colors.billCardText
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        thickness = 2.dp,
                        color = AppTheme.colors.divider
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🚚  Delivery charge",
                            style = AppTheme.textStyles.light.regular,
                            color = AppTheme.colors.billCardText,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            "₹ 40",
                            style = AppTheme.textStyles.regular.large,
                            color = AppTheme.colors.titleText
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        thickness = 2.dp,
                        color = AppTheme.colors.divider
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🛒  Handling fee",
                            style = AppTheme.textStyles.light.regular,
                            color = AppTheme.colors.billCardText,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            "₹ 15",
                            style = AppTheme.textStyles.regular.large,
                            color = AppTheme.colors.titleText
                        )
                    }
                }

                // Always visible content (collapsed state includes this)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "Bill summary",
                        style = AppTheme.textStyles.extraBold.largeTitle,
                        color = AppTheme.colors.titleText,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = 5.dp, start = 2.dp)
                    )
                    // Show "Show details" only in collapsed state
                    if (!scaffoldState.bottomSheetState.isExpanded) {
                        Text(
                            "Show details",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.primary,
                            modifier = Modifier.clickable {
                                scope.launch { scaffoldState.bottomSheetState.expand() }
                            }
                        )
                    } else {
                        Text(
                            "Show less",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.primary,
                            modifier = Modifier.clickable {
                                scope.launch { scaffoldState.bottomSheetState.collapse() }
                            }
                        )
                    }
                }
                Spacer(Modifier.size(120.dp))
            }
        },
        content = {
            sheetContent()
        }
    )

}


@Composable
fun CartItemCard(
    cartItem: CartItem,
    isLoadingMinus: Boolean = false,
    isLoadingPlus: Boolean,
    onRemove: () -> Unit = {},
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {},
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        cartItem.productImage?.let { blob ->
            val bitmap = remember(blob) { // Cache bitmap decoding
                BitmapFactory.decodeByteArray(blob.toBytes(), 0, blob.toBytes().size)
            }
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Product Image",
                    filterQuality = FilterQuality.Low,
                    modifier = Modifier.size(70.dp),
                )
            }
        } ?: Text(
            text = "No image",
            modifier = Modifier
                .size(80.dp)
                .padding(end = 16.dp),
        )
        Column(
            modifier = Modifier.padding(start = 15.dp)
        ) {
            Text(
                cartItem.productName,
                style = AppTheme.textStyles.bold.largeTitle,
                color = AppTheme.colors.titleText,
                fontSize = 19.sp
            )
            Text(
                displayQuantity(cartItem),
                style = AppTheme.textStyles.regular.regular,
                color = AppTheme.colors.lightGray,
                modifier = Modifier.padding(top = 3.dp)
            )
            Row(
                Modifier.padding(top = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SquareBorderProgressIndicator(
                    isLoading = isLoadingMinus,
                    borderWidth = 1.8.dp,
                    iconTint = AppTheme.colors.lightGray,
                    iconSize = 25.dp,
                    size = 35,
                    cornerRadius = 10.dp,
                    onClick = { if (isLoadingPlus) null else onDecrement() },
                    icon = R.drawable.ic_remove
                )

                Text(
                    cartItem.quantity.toString(),
                    style = AppTheme.textStyles.bold.largeTitle,
                    color = AppTheme.colors.titleText,
                    modifier = Modifier.padding(horizontal = 17.dp),
                    fontSize = 20.sp
                )

                SquareBorderProgressIndicator(
                    isLoading = isLoadingPlus,
                    borderWidth = 1.8.dp,
                    iconSize = 25.dp,
                    size = 35,
                    cornerRadius = 10.dp,
                    onClick = { onIncrement() },
                    icon = R.drawable.ic_add
                )

            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            CustomIcon(
                icon = R.drawable.ic_close,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .clickable { onRemove() },
                imageModifier = Modifier.size(25.dp),
                colorFilter = ColorFilter.tint(AppTheme.colors.minusGray)
            )

            Text(
                "₹ ${cartItem.productPrice}",
                style = AppTheme.textStyles.bold.largeTitle,
                color = AppTheme.colors.primary,
                fontSize = 21.sp,
                modifier = Modifier.padding(top = 30.dp)
            )
        }

    }
    Divider(
        color = AppTheme.colors.lightGray,
        thickness = 1.dp,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 10.dp)
    )
}