package com.dev.quickcart.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.quickcart.R
import com.dev.quickcart.ui.theme.AppTheme

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    cardColor: Color = AppTheme.colors.cardBackgroundColor,
    cardCorner: Int = 10,
    onClick: () -> Unit = {},
    isClickable: Boolean = true,
    function: @Composable () -> Unit = {},
) {

    Card(
        shape = RoundedCornerShape(cardCorner.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = modifier
            .clip(RoundedCornerShape(cardCorner.dp))
            .then(
                if (isClickable) Modifier.clickable { onClick() } else Modifier
            )
    ) {
        function()
    }
}

@Composable
fun CustomIcon(
    icon: Int,
    modifier: Modifier,
    imageModifier: Modifier,
    isCircle: Boolean = true,
    contentScale: ContentScale = ContentScale.Fit
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(Color.Transparent)
            .then(
                if (isCircle) Modifier.clip(CircleShape) else Modifier
            )

    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = imageModifier.fillMaxSize(),
            contentScale = contentScale
        )

    }
}


@Composable
fun AddButton(modifier: Modifier = Modifier, onAddClick: () -> Unit = {}) {

    Box(
        modifier = modifier.padding(end = 20.dp, bottom = 20.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = { onAddClick() },
            containerColor = AppTheme.colors.primary
        ) {
            Text(
                text = "+",
                fontSize = 35.sp,
                modifier = Modifier.padding(bottom = 5.dp)
            )
        }

    }

}


@Composable
fun MyDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier
            .padding(top = 6.dp)
            .fillMaxWidth(0.9f),
        thickness = 1.5.dp,
        color = Color.LightGray
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressDropDownWidget(
    modifier: Modifier = Modifier,
    addresses: List<String> = listOf("My Flat", "Home", "Office", "Other"),
    initialAddress: String = "My Flat",
    onAddressSelected: (String) -> Unit = {}
) {
    // Track currently selected address
    var selectedAddress by remember { mutableStateOf(initialAddress) }

    // Track whether the bottom sheet is visible
    var showBottomSheet by remember { mutableStateOf(false) }

    // Bottom sheet state
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // If you want partial expand, set this to false
    )

    // ModalBottomSheet for showing list of addresses
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select Address",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                addresses.forEach { address ->
                    Text(
                        text = address,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedAddress = address
                                onAddressSelected(address)
                                showBottomSheet = false
                            }
                            .padding(vertical = 12.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }

    CustomCard(
        modifier = Modifier.width(140.dp),
        cardCorner = 40,
        cardColor = AppTheme.colors.cardBackgroundColor,
        onClick = { showBottomSheet = true },
    ) {
        Row(
            modifier = Modifier
                .padding(7.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomIcon(
                icon = R.drawable.ic_location,
                modifier = Modifier.padding(2.dp),
                imageModifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = selectedAddress,
                style = AppTheme.textStyles.bold.regular,
                color = AppTheme.colors.titleText
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown Arrow"
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    cornerShape: Int = 8,
    hint: String = "Search...",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = { Text(text = hint) },
        singleLine = true,
        shape = RoundedCornerShape(cornerShape.dp),
        leadingIcon = leadingIcon,
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Search"
                    )
                }
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = AppTheme.colors.cardBackgroundColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        )
    )
}


@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    backgroundColor: Color = AppTheme.colors.primary,
    textColor: Color = AppTheme.colors.onPrimary,
    cornerShape: Int = 50,
    onClick: () -> Unit = {}
) {

    Button(
        onClick = { onClick() },
        modifier = modifier
            .height(56.dp),
        shape = RoundedCornerShape(cornerShape.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        )
    ){
        Text(
            text = text,
            style = AppTheme.textStyles.bold.large,
            color = textColor,
            modifier = textModifier
        )
    }



}


