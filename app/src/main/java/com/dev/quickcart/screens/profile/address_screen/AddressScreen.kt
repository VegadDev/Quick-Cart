package com.dev.quickcart.screens.profile.address_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.dev.quickcart.R
import com.dev.quickcart.data.model.UserAddress
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.MyButton
import com.dev.quickcart.screens.profile.CustomAlert
import com.dev.quickcart.ui.theme.AppTheme


@Composable
fun AddressScreen(interActor: AddressInterActor, uiState: AddressUiState) {


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


            Text(
                "My Addresses 🏠",
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

        var showDialog by remember {
            mutableStateOf(false)
        }

        if (showDialog){
            CustomAlert(
                onDismiss = {showDialog = false},
                onConfirm = {
                    showDialog = false
                    //interActor.onLogoutClick()
                },
                message = "Are you sure you want to Delete the address?",
                title = "Delete"
            )
        }

        LazyColumn {
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
            modifier = Modifier.fillMaxWidth(0.9f).align(Alignment.CenterHorizontally).padding(bottom = 20.dp)
        )

    }
}


@Composable
fun AddressCard(
    address: UserAddress,
    onEditClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {}
) {
    CustomCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        cardColor = AppTheme.colors.billCardBg,
        border = BorderStroke(1.dp, AppTheme.colors.borderGray),
        cardElevation = 20,
        cardCorner = 17,
        isClickable = false
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val image: Int = when(address.category){
                    "Home" -> R.drawable.ic_home
                    "Work" -> R.drawable.ic_work
                    "Flat" -> R.drawable.ic_flat
                    else -> R.drawable.ic_other
                }
                CustomIcon(
                    icon = image,
                    modifier = Modifier.padding(start = 5.dp),
                    imageModifier = Modifier.size(27.dp),
                    colorFilter = ColorFilter.tint(AppTheme.colors.titleText),
                    isCircle = false
                )
                Text(
                    address.category,
                    color = AppTheme.colors.titleText,
                    style = AppTheme.textStyles.regular.large,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    "+91 ${address.phoneNumber}",
                    color = AppTheme.colors.lightGray,
                    style = AppTheme.textStyles.regular.large
                )
            }
            Text(
                "${ address.houseAddress }, ${ address.areaAddress } ${address.landmark ?: ""}",
                color = AppTheme.colors.titleText,
                style = AppTheme.textStyles.regular.large,
                modifier = Modifier.padding(top = 10.dp)
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Edit",
                    color = AppTheme.colors.primary,
                    style = AppTheme.textStyles.regular.regular,
                    modifier = Modifier.padding(start = 6.dp).clickable { onEditClick() }
                )
                Text(
                    "Remove",
                    color = AppTheme.colors.error,
                    style = AppTheme.textStyles.regular.regular,
                    modifier = Modifier.padding(start = 10.dp).clickable { onRemoveClick() }
                )
            }

        }
    }

}