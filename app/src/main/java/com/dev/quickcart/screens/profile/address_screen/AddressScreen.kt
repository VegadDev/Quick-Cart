package com.dev.quickcart.screens.profile.address_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.quickcart.R
import com.dev.quickcart.data.model.UserAddress
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.MyButton
import com.dev.quickcart.screens.common.TopBar
import com.dev.quickcart.screens.profile.CustomAlert
import com.dev.quickcart.ui.theme.AppTheme


@Composable
fun AddressScreen(interActor: AddressInterActor, uiState: AddressUiState) {


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TopBar(
            title = "My Addresses 🏠",
            onBackClick = { interActor.onBackClick() }
        )


        var showDialog by remember {
            mutableStateOf(false)
        }

        if (showDialog) {
            CustomAlert(
                onDismiss = { showDialog = false },
                onConfirm = {
                    showDialog = false
                    //interActor.onLogoutClick()
                },
                message = "Are you sure you want to Delete the address?",
                title = "Delete"
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            items(uiState.addresses) { address ->
                AddressCard(
                    address,
                    onRemoveClick = {
                        showDialog = true
                    }
                )
            }
        }


        Spacer(Modifier.weight(1f))
        MyButton(
            "Add New Address",
            onClick = { interActor.onAddAddressClick() },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 30.dp),
            cornerShape = 17,
            icon = {
                CustomIcon(
                    icon = R.drawable.ic_add,
                    modifier = Modifier.padding(end = 20.dp),
                    imageModifier = Modifier.size(25.dp),
                    isCircle = false,
                    colorFilter = ColorFilter.tint(AppTheme.colors.white)
                )
            }
        )

    }
}


@Composable
fun AddressCard(
    address: UserAddress,
    onEditClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {},
) {
    CustomCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        cardElevation = 20,
        cardCorner = 17,
        isClickable = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
            ) {

                Column(
                    modifier = Modifier.padding(top = 5.dp)
                ) {
                    val image: Int = when (address.category) {
                        "Home" -> R.drawable.ic_home
                        "Work" -> R.drawable.ic_work
                        "Flat" -> R.drawable.ic_flat
                        else -> R.drawable.ic_other
                    }
                    CustomIcon(
                        icon = image,
                        modifier = Modifier,
                        imageModifier = Modifier.size(25.dp),
                        colorFilter = ColorFilter.tint(AppTheme.colors.primary),
                        isCircle = false
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .weight(1f)
                ) {
                    Text(
                        address.category,
                        color = AppTheme.colors.primaryText,
                        style = AppTheme.textStyles.regular.large,
                        modifier = Modifier
                    )
                    Text(
                        "${address.houseAddress}, ${address.areaAddress} ${address.landmark ?: ""}",
                        color = AppTheme.colors.secondaryText,
                        style = AppTheme.textStyles.regular.regular,
                        fontSize = 15.sp,
                        modifier = Modifier
                    )
                    Text(
                        "+91 ${address.phoneNumber}",
                        color = AppTheme.colors.secondaryText,
                        fontSize = 15.sp,
                        style = AppTheme.textStyles.regular.regular
                    )
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(AppTheme.colors.edit_bg)
                        .fillMaxWidth()
                        .weight(1f)
                        .height(40.dp)
                        .clickable { onEditClick() }
                ) {
                    CustomIcon(
                        icon = R.drawable.edit,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable { onEditClick() },
                        imageModifier = Modifier.size(25.dp),
                        isCircle = false,
                        colorFilter = ColorFilter.tint(AppTheme.colors.brandText)
                    )
                    Text(
                        "Edit",
                        color = AppTheme.colors.brandText,
                        style = AppTheme.textStyles.regular.regular,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(AppTheme.colors.delete_bg)
                        .fillMaxWidth()
                        .weight(1f)
                        .height(40.dp)
                        .clickable { onRemoveClick() }
                ) {
                    CustomIcon(
                        icon = R.drawable.delete,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable { onRemoveClick() },
                        imageModifier = Modifier.size(22.dp),
                        isCircle = false,
                        colorFilter = ColorFilter.tint(AppTheme.colors.error)
                    )
                    Text(
                        "Delete",
                        color = AppTheme.colors.error,
                        style = AppTheme.textStyles.regular.regular,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

            }


        }
    }

}