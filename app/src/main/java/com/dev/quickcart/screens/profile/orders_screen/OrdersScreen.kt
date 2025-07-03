package com.dev.quickcart.screens.profile.orders_screen

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.quickcart.R
import com.dev.quickcart.data.model.CartItem
import com.dev.quickcart.data.model.UserAddress
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.TopBar
import com.dev.quickcart.ui.theme.AppTheme
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OrderScreen(interActor: OrdersInterActor, uiState: OrdersUiState) {


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TopBar(
            title = "My Orders",
            onBackClick = { interActor.onBackClick() }
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


@Preview
@Composable
private fun Abc() {
    OrderCard(
        address = UserAddress(
            phoneNumber = "1234567890",
            houseAddress = "123 Main Street",
            areaAddress = "Apt 4B, New York",
            category = "Home"
        ),
        cartItems = listOf(
            CartItem(
                productName = "Product 1",
                productPrice = 10.0,
                quantity = 2,
                productImage = null
            )
        ),
        totalPrice = 20.0,
        status = "Pending"
    )
}



@Composable
fun OrderCard(
    address: UserAddress? = null,
    cartItems: List<CartItem>,
    totalPrice: Double,
    orderDate: Timestamp = Timestamp.now(),
    status: String,
) {

    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(orderDate.toDate())

    CustomCard(
        modifier = Modifier
            .fillMaxWidth(),
        cardColor = AppTheme.colors.billCardBg,
        cardElevation = 20,
    ) {
        Column(
            modifier = Modifier.background(AppTheme.colors.topbar_bg).fillMaxWidth()
        ) {
            Row {

                Column(
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Text(
                        "ORD-2023-101",
                        style = AppTheme.textStyles.regular.small,
                        color = AppTheme.colors.white
                    )

                    Text(
                        formattedDate,
                        style = AppTheme.textStyles.regular.small,
                        color = AppTheme.colors.white
                    )
                }

            }
        }


        CustomCard(
            cardColor = AppTheme.colors.cardBackgroundColor,
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                CustomIcon(
                    icon = R.drawable.ic_fiber,
                    modifier = Modifier,
                    imageModifier = Modifier.size(40.dp)
                )
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Text(
                        "Fresh Avocados",
                        style = AppTheme.textStyles.regular.small,
                        color = AppTheme.colors.white
                    )
                    Row {
                        Text(
                            "x2",
                            style = AppTheme.textStyles.regular.small,
                            color = AppTheme.colors.white
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            "$199",
                            style = AppTheme.textStyles.regular.small,
                            color = AppTheme.colors.white
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            address?.let {
                Text(
                    "${it.houseAddress}, ${it.areaAddress}",
                    style = AppTheme.textStyles.regular.small,
                    color = AppTheme.colors.white
                )
            }
            address?.let {
                Text(
                    "Mo. ${it.phoneNumber}",
                    style = AppTheme.textStyles.regular.small,
                    color = AppTheme.colors.minusGray
                )
            }

            Row {
                Text(
                    "Total Amount",
                    style = AppTheme.textStyles.regular.regular,
                    color = AppTheme.colors.white
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "₹ $totalPrice",
                    style = AppTheme.textStyles.regular.regular,
                    color = AppTheme.colors.white
                )
            }
            Divider(
                color = AppTheme.colors.lightGray,
                thickness = 1.dp,
                modifier = Modifier
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    formattedDate,
                    style = AppTheme.textStyles.regular.regular,
                    color = AppTheme.colors.white
                )
                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier.background(AppTheme.colors.topbar_bg),
                    horizontalArrangement = Arrangement.End
                ) {
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
                            color = AppTheme.colors.white
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
                        color = AppTheme.colors.white
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
                                color = AppTheme.colors.white
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
                                color = AppTheme.colors.white,
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                "${cartItems.productPrice}",
                                style = AppTheme.textStyles.regular.regular,
                                color = AppTheme.colors.white
                            )
                        }
                    }
                }

                Row {
                    Text(
                        "Total Amount",
                        style = AppTheme.textStyles.regular.regular,
                        color = AppTheme.colors.white
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "₹ $totalPrice",
                        style = AppTheme.textStyles.regular.regular,
                        color = AppTheme.colors.white
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
                        color = AppTheme.colors.white
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