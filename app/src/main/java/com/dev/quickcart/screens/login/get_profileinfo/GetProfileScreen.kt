package com.dev.quickcart.screens.login.get_profileinfo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.CustomTextField
import com.dev.quickcart.ui.theme.AppTheme
import com.dev.quickcart.R
import com.dev.quickcart.screens.common.MyButton
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GetProfileScreen(interActor: GetProfileInterActor , uiState: GetProfileUiState) {


    val categoryOptions = listOf("Home", "Work", "Flat", "Other")
    val selectedCategory = remember { mutableStateOf(uiState.category) }
    LocalConfiguration.current.screenWidthDp


    Column(
        modifier = Modifier.fillMaxSize().padding(15.dp)
    ){

        Text(
            "Enter UserName",
            style = AppTheme.textStyles.bold.large,
            color = AppTheme.colors.titleText
        )
        Spacer(Modifier.size(10.dp))
        CustomTextField(
            value = uiState.userName,
            onValueChange = { interActor.updateUserName(it) },
            onSearch = {},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            error = uiState.userNameError,
            hint = "Type Here"
        )
        Spacer(Modifier.size(20.dp))

        Text(
            "Enter Phone Number",
            style = AppTheme.textStyles.bold.large,
            color = AppTheme.colors.titleText
        )
        Spacer(Modifier.size(10.dp))

        CustomTextField(
            value = uiState.mobileNumber,
            onValueChange = {
                val sanitizedValue =
                    it.filter { it.isDigit() } // Keep only numbers (remove spaces)
                if (sanitizedValue.length <= 10) { // Restrict to 10 digits (excluding spaces)
                    interActor.updateMobileNumber(sanitizedValue)
                }
            },
            onSearch = { },
            hint = "(000) 000-00-00",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Go,
                keyboardType = KeyboardType.Number
            ),
            prefix = {
                Text(
                    "+91  ",
                    style = AppTheme.textStyles.extraBold.large,
                    color = AppTheme.colors.titleText,
                    modifier = Modifier.padding(start = 13.dp)
                )
            }
        )



        Spacer(Modifier.size(20.dp))

        Text(
            "House / Flat / Block NO.",
            style = AppTheme.textStyles.bold.large,
            color = AppTheme.colors.titleText
        )
        Spacer(Modifier.size(10.dp))
        CustomTextField(
            value = uiState.houseAddress,
            onValueChange = { interActor.updateHouseAddress(it) },
            onSearch = {},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            error = uiState.houseAddressError,
            hint = "Type Here"
        )


        Spacer(Modifier.size(20.dp))

        Text(
            "Area / Road / Apartment",
            style = AppTheme.textStyles.bold.large,
            color = AppTheme.colors.titleText
        )
        Spacer(Modifier.size(10.dp))
        CustomTextField(
            value = uiState.areaAddress,
            onValueChange = { interActor.updateAreaAddress(it) },
            onSearch = {},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            error = uiState.areaAddressError,
            hint = "Type Here"
        )

        Spacer(Modifier.size(20.dp))

        Text(
            "Landmark (Optional)",
            style = AppTheme.textStyles.bold.large,
            color = AppTheme.colors.titleText
        )
        Spacer(Modifier.size(10.dp))
        CustomTextField(
            value = uiState.landmarkAddress,
            onValueChange = { interActor.updateLandmarkAddress(it) },
            onSearch = {},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            error = uiState.landmarkAddressError,
            hint = "Type Here"
        )

        Spacer(Modifier.size(20.dp))

        Text(
            "Save As",
            style = AppTheme.textStyles.bold.large,
            color = AppTheme.colors.titleText
        )


        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categoryOptions) { category ->
                CategoryCard(
                    category = category,
                    isSelected = selectedCategory.value == category,
                    onClick = {
                        if (!uiState.isLoading) {
                            selectedCategory.value = category
                            interActor.updateCategory(category)
                        }
                    },
                    //modifier = Modifier.width(cardWidth)
                )
            }
        }


        MyButton(
            "Submit",
            onClick = { interActor.submit()},
            modifier = Modifier.fillMaxWidth(0.9f).align(Alignment.CenterHorizontally)
        )


        uiState.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        if (uiState.isSaved) {
            Text(
                text = "Address saved successfully!",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            LaunchedEffect(Unit) {
                delay(1000) // Show success message for 1 second
                //onAddressSaved()
            }
        }

    }


}



@Composable
fun CategoryCard(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CustomCard(
        modifier = modifier
            .width(90.dp)
            .height(50.dp),
        onClick = { onClick() },
        cardColor =  if (isSelected) AppTheme.colors.primary else AppTheme.colors.background,
        border = BorderStroke(1.dp, AppTheme.colors.borderGray),
        cardElevation = 10,
        cardCorner = 20
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
