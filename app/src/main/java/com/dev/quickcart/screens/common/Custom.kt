package com.dev.quickcart.screens.common

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.dev.quickcart.R
import com.dev.quickcart.screens.home.HomeUiState
import com.dev.quickcart.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    cardColor: Color = AppTheme.colors.cardBackgroundColor,
    cardCorner: Int = 10,
    onClick: () -> Unit = {},
    isClickable: Boolean = true,
    border: BorderStroke? = null,
    cardElevation: Int = 5,
    function: @Composable () -> Unit = {},
) {

    Card(
        shape = RoundedCornerShape(cardCorner.dp),
        elevation = CardDefaults.cardElevation(cardElevation.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = border,
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
    contentScale: ContentScale = ContentScale.Fit,
    colorFilter: ColorFilter? = null
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
            contentScale = contentScale,
            colorFilter = colorFilter
        )

    }
}

@Preview(showBackground = true)
@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {},
    homeUiState: HomeUiState = HomeUiState()
) {

    Box(
        modifier = Modifier
    ) {
        if (homeUiState.isLoadingOnATC) {
            Box(
                modifier = Modifier
            ) {
                CustomCard(
                    isClickable = false,
                    modifier = Modifier,
                    cardCorner = 40,
                    cardColor = AppTheme.colors.primary
                ) {
                    CustomIcon(
                        icon = R.drawable.ic_add,
                        modifier = Modifier.padding(5.dp),
                        imageModifier = Modifier.size(30.dp),
                        colorFilter = ColorFilter.tint(AppTheme.colors.titleText)
                    )
                }
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = AppTheme.colors.titleText
                )
            }
        }
        else {
            CustomCard(
                onClick = { onAddClick() },
                modifier = Modifier,
                cardCorner = 40,
                cardColor = AppTheme.colors.primary
            ) {
                CustomIcon(
                    icon =  R.drawable.ic_add,
                    modifier = Modifier.padding(5.dp),
                    imageModifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(AppTheme.colors.titleText)
                )
            }
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


@Composable
fun NewCard(
    modifier: Modifier = Modifier,
    cardWidth: Int = 0,
    cardHeight: Int = 0,
    cardCorner: Int = 15,
    onClick: () -> Unit = {},
    cardColor: Color = AppTheme.colors.cardBackgroundColor,
    function: @Composable () -> Unit = {},
) {
    var isPressed by remember { mutableStateOf(false) }

    // Animating offset values for smooth movement
    val offsetX by animateDpAsState(
        targetValue = if (isPressed) 6.dp else 0.dp,
        animationSpec = tween(durationMillis = 150), label = "offsetX"
    )
    val offsetY by animateDpAsState(
        targetValue = if (isPressed) 6.dp else 0.dp,
        animationSpec = tween(durationMillis = 150), label = "offsetY"
    )

    Box(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    isPressed = true
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(150) //pressed effect
                        isPressed = false
                    }
                    onClick()
                }
            )
    ) {
        val tempWidth = cardWidth + 5
        val tempHeight = cardHeight + 5

        Box(
            modifier = Modifier
                .width(tempWidth.dp)
                .height(tempHeight.dp)
                .padding(top = 5.dp, start = 5.dp)
                .background(AppTheme.colors.cardShadow, shape = RoundedCornerShape(cardCorner.dp))
        )


        Box(
            modifier = Modifier
                .width(cardWidth.dp)
                .height(cardHeight.dp)
                .offset(x = offsetX, y = offsetY) // Moves smoothly when clicked
                .border(
                    border = BorderStroke(width = 2.dp, color = AppTheme.colors.cardShadow),
                    shape = RoundedCornerShape(cardCorner.dp)
                )
                .background(cardColor, shape = RoundedCornerShape(cardCorner.dp)),

            contentAlignment = Alignment.Center
        ) {
            function()
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDropDown(
    modifier: Modifier = Modifier,
    items: List<String>,
    initialItem: String = items[0],
    onItemSelected: (String) -> Unit = {},
    title: String = "",
    isicon: Boolean = true,
    corner: Int = 30
) {
    // Track currently selected address
    var selectedItem by remember { mutableStateOf(initialItem) }

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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = AppTheme.textStyles.bold.largeTitle,
                        modifier = Modifier
                            .padding(bottom = 8.dp),
                    )
                    Spacer(Modifier.weight(1f))
                    if (title.equals("Select Address")){
                        Text(
                            text = "+ Add new address",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.primary,
                            modifier = Modifier.clickable { },
                        )
                    }
                    else null
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    this.items(items.chunked(2)) { rowItems ->  // Group into pairs
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            rowItems.forEach { item ->
                                CustomCard(
                                    border = if (selectedItem == item) BorderStroke(1.dp, AppTheme.colors.primary) else null,
                                    onClick = {
                                        selectedItem = item
                                        onItemSelected(item)
                                    },
                                    modifier = Modifier.weight(1f).padding(6.dp),
                                    cardCorner = 14
                                ) {
                                    Text(
                                        item,
                                        style = AppTheme.textStyles.bold.large,
                                        color = if (selectedItem == item) AppTheme.colors.primary else AppTheme.colors.titleText,
                                        modifier = Modifier.padding(horizontal = 15.dp , vertical = 10.dp)
                                    )
                                }
                            }
                            if (rowItems.size < 2) {
                                Spacer(modifier = Modifier.weight(1f)) // Maintain layout if odd items
                            }
                        }
                    }
                }
                Spacer(Modifier.size(15.dp))
                MyButton(
                    "Confirm",
                    onClick = { showBottomSheet = false },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

            }
        }
    }

    NewCard(
        cardWidth = 140,
        cardHeight = 55,
        cardCorner = corner ,
        cardColor = AppTheme.colors.cardBackgroundColor,
        onClick = { showBottomSheet = true },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(7.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isicon){
                CustomIcon(
                    icon = R.drawable.ic_location,
                    modifier = Modifier.padding(2.dp),
                    imageModifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(AppTheme.colors.primary)
                )

            }
            else null
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = selectedItem,
                style = AppTheme.textStyles.bold.regular,
                color = AppTheme.colors.titleText
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    cornerShape: Int = 8,
    hint: String = "",
    error: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    textStyle: TextStyle = AppTheme.textStyles.bold.regular,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textFieldColors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = AppTheme.colors.cardBackgroundColor,
        unfocusedContainerColor = AppTheme.colors.cardBackgroundColor,
        disabledContainerColor = AppTheme.colors.cardBackgroundColor,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    )
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = { Text(
            text = hint,
            style = AppTheme.textStyles.extraBold.large,
            color = AppTheme.colors.lightGray
        ) },
        singleLine = true,
        isError = error?.isNotEmpty() ?: false,
        shape = RoundedCornerShape(cornerShape.dp),
        textStyle = textStyle,
        leadingIcon = leadingIcon,
        trailingIcon = {
            if(value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Search"
                    )
                }
            }
        },
        colors = textFieldColors,
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            }
        ),
        keyboardOptions = keyboardOptions,
        prefix = prefix
    )
}


@Composable
fun MyButton(
    text: String,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    backgroundColor: Color = AppTheme.colors.primary,
    textColor: Color = AppTheme.colors.onPrimary,
    cornerShape: Int = 50,
    icon: @Composable () -> Unit = {},
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

        Row() {
            icon()
            Text(
                text = text,
                style = AppTheme.textStyles.bold.large,
                color = textColor,
                modifier = textModifier
            )

        }



    }
}


@Composable
fun ImagePicker(
    onImagePicked: (Uri?) -> Unit, // Callback to handle the picked Uri
    modifier: Modifier = Modifier
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher for picking an image
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        onImagePicked(uri) // Pass the Uri to the callback
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text("Pick Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Optional: Preview the selected image
        imageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
            Text(
                text = "Selected: ${uri.path}",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

