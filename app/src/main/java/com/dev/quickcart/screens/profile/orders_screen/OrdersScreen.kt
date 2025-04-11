package com.dev.quickcart.screens.profile.orders_screen

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dev.quickcart.R
import com.dev.quickcart.data.model.CartItem
import com.dev.quickcart.data.model.UserAddress
import com.dev.quickcart.screens.common.AddButton
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.NewCard
import com.dev.quickcart.ui.theme.AppTheme
import com.dev.quickcart.utils.displayQuantity
import com.google.firebase.Timestamp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OrderScreen(interActor: OrdersInterActor, uiState: OrdersUiState) {


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = 100.dp),
        ) {

            CustomIcon(
                icon = R.drawable.ic_back_arrow,
                modifier = Modifier
                    .padding(start = 16.dp, top = 15.dp)
                    .clickable { interActor.onBackClick() },
                imageModifier = Modifier.size(25.dp),
                colorFilter = ColorFilter.tint(AppTheme.colors.titleText)
            )


            androidx.compose.material.Text(
                "My Orders 📦",
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


        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(uiState.orders) { order ->

                OrderCard(
                    address = order.address,
                    cartItems = order.cartItems,
                    totalPrice = order.totalPrice,
                    orderDate = order.orderDate,
                    status = order.status
                )

            }
        }


    }


}


@Composable
fun OrderCard(
    address: UserAddress? = null,
    cartItems: List<CartItem>,
    totalPrice: Double,
    orderDate: Timestamp,
    status: String,
) {

    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(orderDate.toDate())

    CustomCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        cardColor = AppTheme.colors.billCardBg,
        cardElevation = 20,
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Address Section
            Row {
                address?.let {
                    Text(
                        "Shipping Address - ${it.category}",
                        style = AppTheme.textStyles.regular.small,
                        color = AppTheme.colors.titleText
                    )
                }
                Spacer(Modifier.weight(1f))
                address?.let {
                    Text(
                        "Mo. ${it.phoneNumber}",
                        style = AppTheme.textStyles.regular.small,
                        color = AppTheme.colors.minusGray
                    )
                }
            }
            address?.let {
                Text(
                    "${it.houseAddress}, ${it.areaAddress}",
                    style = AppTheme.textStyles.regular.small,
                    color = AppTheme.colors.titleText
                )
            }

            // Cart Items Header
            Text(
                "Total Items :",
                style = AppTheme.textStyles.regular.regular,
                color = AppTheme.colors.primary
            )

            // LazyColumn with bounded height
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp), // Bounded height to avoid infinite constraints
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems) { cartItem ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        cartItem.productImage?.let { blob ->
                            val bitmap = remember(blob) {
                                BitmapFactory.decodeByteArray(blob.toBytes(), 0, blob.toBytes().size)
                            }
                            bitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Product Image",
                                    filterQuality = FilterQuality.Low,
                                    modifier = Modifier.size(50.dp),
                                )
                            }
                        } ?: Text(
                            text = "No image",
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 16.dp),
                        )
                        Spacer(Modifier.weight(0.1f))

                        Text(
                            cartItem.productName,
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.titleText
                        )
                        Spacer(Modifier.weight(0.1f))
                        Text(
                            "x",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.lightGray,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        Text(
                            "${cartItem.quantity}",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.titleText,
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            "${cartItem.productPrice}",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.titleText
                        )
                    }
                }
            }

            // Total Amount Section
            Row {
                Text(
                    "Total Amount",
                    style = AppTheme.textStyles.regular.regular,
                    color = AppTheme.colors.titleText
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "₹ $totalPrice",
                    style = AppTheme.textStyles.regular.regular,
                    color = AppTheme.colors.titleText
                )
            }
            Divider(
                color = AppTheme.colors.lightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 5.dp)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    formattedDate,
                    style = AppTheme.textStyles.regular.regular,
                    color = AppTheme.colors.titleText
                )
                Spacer(Modifier.weight(1f))
                Text(
                    status,
                    style = AppTheme.textStyles.regular.regular,
                    color = AppTheme.colors.primary
                )
            }
        }
    }
}




@Composable
fun OrderCard1(
    address: UserAddress? = null,
    cartItems: List<CartItem>,
    totalPrice: Double,
    orderDate: Timestamp,
    status: String,
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        CustomCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            cardColor = AppTheme.colors.billCardBg,
            cardElevation = 20,
        ) {
            Column(
                modifier = Modifier.padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row() {
                    address?.let {
                        Text(
                            "Shipping Address - ${it.category}",
                            style = AppTheme.textStyles.regular.small,
                            color = AppTheme.colors.titleText
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    address?.let {
                        Text(
                            "Mo. ${it.phoneNumber}",
                            style = AppTheme.textStyles.regular.small,
                            color = AppTheme.colors.minusGray
                        )
                    }

                }
                address?.let {
                    Text(
                        "${it.houseAddress}, ${it.areaAddress}",
                        style = AppTheme.textStyles.regular.small,
                        color = AppTheme.colors.titleText
                    )
                }

                Text(
                    "Total Items :",
                    style = AppTheme.textStyles.regular.regular,
                    color = AppTheme.colors.primary
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(cartItems) {cartItems->
                        Row {
                            cartItems.productImage?.let { blob ->
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
                            Text(
                                cartItems.productName,
                                style = AppTheme.textStyles.regular.regular,
                                color = AppTheme.colors.titleText
                            )
                            Spacer(Modifier.weight(0.1f))
                            Text(
                                "x",
                                style = AppTheme.textStyles.regular.regular,
                                color = AppTheme.colors.lightGray,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            Text(
                                "${cartItems.quantity}",
                                style = AppTheme.textStyles.regular.regular,
                                color = AppTheme.colors.titleText,
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                "${cartItems.productPrice}",
                                style = AppTheme.textStyles.regular.regular,
                                color = AppTheme.colors.titleText
                            )
                        }
                    }
                }

                Row {
                    Text(
                        "Total Amount",
                        style = AppTheme.textStyles.regular.regular,
                        color = AppTheme.colors.titleText
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "₹ $totalPrice",
                        style = AppTheme.textStyles.regular.regular,
                        color = AppTheme.colors.titleText
                    )
                }
                Divider(
                    color = AppTheme.colors.lightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 5.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "$orderDate",
                        style = AppTheme.textStyles.regular.regular,
                        color = AppTheme.colors.titleText
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        status,
                        style = AppTheme.textStyles.regular.regular,
                        color = AppTheme.colors.primary
                    )

                }
            }

        }


    }


}