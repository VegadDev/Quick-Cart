package com.dev.quickcart.screens.common

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.dev.quickcart.R
import com.dev.quickcart.data.model.UserAddress
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
    color: Color = AppTheme.colors.primary,
    onAddClick: () -> Unit = {},
    isLoading: Boolean = false,
) {


    Box(
        modifier = Modifier
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
            ) {
                CustomCard(
                    isClickable = false,
                    modifier = modifier,
                    cardCorner = 40,
                    cardColor = color
                ) {
                    CustomIcon(
                        icon = R.drawable.ic_add,
                        modifier = Modifier.padding(5.dp),
                        imageModifier = Modifier.size(30.dp),
                        colorFilter = ColorFilter.tint(AppTheme.colors.white)
                    )
                }
                CircularProgressIndicator(
                    modifier = Modifier.size(34.dp),
                    color = AppTheme.colors.white
                )
            }
        } else {
            CustomCard(
                onClick = { onAddClick() },
                modifier = modifier,
                cardCorner = 40,
                cardColor = color
            ) {
                CustomIcon(
                    icon = R.drawable.ic_add,
                    modifier = Modifier.padding(5.dp),
                    imageModifier = Modifier.size(32.dp),
                    colorFilter = ColorFilter.tint(AppTheme.colors.white)
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
fun MyDropDown1(
    modifier: Modifier = Modifier,
    items: List<String>,
    initialItem: String = "",
    onItemSelected: (String) -> Unit = {},
    onNewAddress: () -> Unit = {},
    title: String = "",
    isIcon: Boolean = true,
    showBottomSheet: Boolean = false,
    corner: Int = 30
) {
    // State for the bottom sheet visibility
    var isBottomSheetVisible by remember { mutableStateOf(showBottomSheet) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // State for the selected item, initialized with initialItem
    var selectedItem by remember { mutableStateOf(initialItem) }

    // ModalBottomSheet for showing list of addresses (Your design)
    if (isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isBottomSheetVisible = false },
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
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = AppTheme.textStyles.bold.largeTitle,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    if (title.equals("Select Address", ignoreCase = true)) {
                        Text(
                            text = "+ Add new address",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.primary,
                            modifier = Modifier.clickable {
                                onNewAddress()
                                isBottomSheetVisible = false // Optionally close sheet
                            }
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    items(items.chunked(2)) { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            rowItems.forEach { item ->
                                val cleanItem = item.trim() // Ensure no whitespace
                                CustomCard(
                                    border = if (selectedItem == cleanItem) BorderStroke(
                                        1.dp,
                                        AppTheme.colors.primary
                                    ) else null,
                                    onClick = {
                                        selectedItem = cleanItem
                                        Log.d("MyDropDown", "Selected clean item: '$cleanItem'")
                                        onItemSelected(cleanItem) // Pass clean string
                                        isBottomSheetVisible = false // Close sheet on selection
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(6.dp),
                                    cardCorner = 14
                                ) {
                                    Text(
                                        text = cleanItem,
                                        style = AppTheme.textStyles.bold.large,
                                        color = if (selectedItem == cleanItem) AppTheme.colors.primary else AppTheme.colors.white,
                                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
                                    )
                                }
                            }
                            if (rowItems.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
                Spacer(Modifier.size(15.dp))
                MyButton(
                    text = "Confirm",
                    onClick = { isBottomSheetVisible = false },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }
    }

    // Dropdown trigger card (Your design)
    NewCard(
        cardWidth = 140,
        cardHeight = 55,
        cardCorner = corner,
        cardColor = AppTheme.colors.cardBackgroundColor,
        onClick = { isBottomSheetVisible = true },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(7.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isIcon) {
                CustomIcon(
                    icon = R.drawable.ic_location,
                    modifier = Modifier.padding(2.dp),
                    imageModifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(AppTheme.colors.primary)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (selectedItem.isEmpty()) "Select Address" else selectedItem,
                style = AppTheme.textStyles.bold.regular,
                color = AppTheme.colors.white
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
fun MyDropDown(
    modifier: Modifier = Modifier,
    items: List<UserAddress>,
    initialItem: String = "",
    onItemSelected: (String) -> Unit = {},
    onNewAddress: () -> Unit = {},
    title: String = "",
    isIcon: Boolean = true,
    showBottomSheet: Boolean = false, // Controlled by HomeScreen
    corner: Int = 30
) {
    // State for bottom sheet visibility, initialized with showBottomSheet
    var isBottomSheetVisible by remember { mutableStateOf(showBottomSheet) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // State for the selected item
    var selectedItem by remember { mutableStateOf(initialItem) }

    // ModalBottomSheet for showing list of addresses
    if (isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isBottomSheetVisible = false },
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
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = AppTheme.textStyles.bold.largeTitle,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    if (title.equals("Select Address", ignoreCase = true)) {
                        Text(
                            text = "+ Add new address",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.primary,
                            modifier = Modifier.clickable {
                                onNewAddress()
                                isBottomSheetVisible = false
                            }
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    items(items) { address ->
                        val cleanCategory = address.category.trim()
                        CustomCard(
                            border = if (selectedItem == cleanCategory) BorderStroke(
                                1.dp,
                                AppTheme.colors.primary
                            ) else null,
                            onClick = {
                                selectedItem = cleanCategory
                                Log.d("MyDropDown", "Selected category: '$cleanCategory'")
                                onItemSelected(cleanCategory)
                                isBottomSheetVisible = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            cardCorner = 14
                        ) {
                            Column(
                                modifier = Modifier.padding(15.dp)
                            ) {
                                Text(
                                    text = cleanCategory,
                                    style = AppTheme.textStyles.bold.large,
                                    color = if (selectedItem == cleanCategory) AppTheme.colors.primary else AppTheme.colors.white
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${address.houseAddress}, ${address.areaAddress}",
                                    style = AppTheme.textStyles.regular.regular,
                                    color = AppTheme.colors.white
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    NewCard(
        cardWidth = 140,
        cardHeight = 55,
        cardCorner = corner,
        cardColor = AppTheme.colors.cardBackgroundColor,
        onClick = { isBottomSheetVisible = true },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(7.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isIcon) {
                CustomIcon(
                    icon = R.drawable.ic_location,
                    modifier = Modifier.padding(2.dp),
                    imageModifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(AppTheme.colors.primary)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (selectedItem.isEmpty()) "Select Address" else selectedItem,
                style = AppTheme.textStyles.bold.regular,
                color = AppTheme.colors.white
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
fun MyDropDown5(
    modifier: Modifier = Modifier,
    items: List<UserAddress>,
    initialItem: String = "",
    onItemSelected: (String) -> Unit = {},
    onNewAddress: () -> Unit = {},
    title: String = "",
    showBottomSheet: Boolean = false
) {
    // State for bottom sheet visibility
    var isBottomSheetVisible by remember { mutableStateOf(showBottomSheet) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // State for the selected item
    var selectedItem by remember { mutableStateOf(initialItem) }

    // ModalBottomSheet for address list
    if (isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isBottomSheetVisible = false },
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
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = AppTheme.textStyles.bold.largeTitle,
                        modifier = Modifier.padding(8.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    if (title.equals("Select Address", ignoreCase = true)) {
                        Text(
                            text = "+ Add new address",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.primary,
                            modifier = Modifier.clickable {
                                onNewAddress()
                                isBottomSheetVisible = false
                            }
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    items(items) { address ->
                        val cleanCategory = address.category.trim()
                        CustomCard(
                            border = if (selectedItem == cleanCategory) BorderStroke(
                                1.dp,
                                AppTheme.colors.primary
                            ) else null,
                            onClick = {
                                selectedItem = cleanCategory
                                Log.d("MyDropDown", "Selected category: '$cleanCategory'")
                                onItemSelected(cleanCategory)
                                isBottomSheetVisible = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            cardCorner = 14
                        ) {
                            Column(
                                modifier = Modifier.padding(15.dp)
                            ) {
                                Text(
                                    text = cleanCategory,
                                    style = AppTheme.textStyles.bold.large,
                                    color = if (selectedItem == cleanCategory) AppTheme.colors.primary else AppTheme.colors.white
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${address.houseAddress}, ${address.areaAddress}",
                                    style = AppTheme.textStyles.regular.regular,
                                    color = AppTheme.colors.white
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Dropdown trigger styled to match the image
    Surface(
        modifier = modifier
            .width(130.dp)
            .height(40.dp)
            .clickable { isBottomSheetVisible = true },
        shape = RoundedCornerShape(20.dp),
        color = AppTheme.colors.address_bg,
        contentColor = AppTheme.colors.address_text,
        //border = BorderStroke(0.2.dp, AppTheme.colors.primary)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.location),
                    contentDescription = "Location Icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Selected item or placeholder text
                Text(
                    text = if (selectedItem.isEmpty()) "Select Address" else selectedItem,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
            // Downward caret icon
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown Arrow",
                modifier = Modifier.size(24.dp),
                tint = AppTheme.colors.address_text
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
        placeholder = {
            Text(
                text = hint,
                style = AppTheme.textStyles.extraBold.large,
                color = AppTheme.colors.lightGray
            )
        },
        singleLine = true,
        isError = error?.isNotEmpty() == true,
        shape = RoundedCornerShape(cornerShape.dp),
        textStyle = textStyle,
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
    ) {

        Row {
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


@Composable
fun SquareBorderProgressIndicator(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onClick: () -> Unit = {},
    icon: Int,
    iconTint: Color = AppTheme.colors.primary,
    iconSize: Dp = 17.5.dp,
    staticBorderColor: Color = AppTheme.colors.minusGray,
    staticBorderLoadingColor: Color = AppTheme.colors.minusGray,
    animatedBorderColor: Color = AppTheme.colors.primary,
    backgroundColor: Color = AppTheme.colors.background,
    borderWidth: Dp = 1.5.dp,
    size: Int = 35,
    cornerRadius: Dp = 10.dp,
    animationDurationMillis: Int = 800
) {
    // Convert Dp to pixels inside the composable using LocalDensity
    val density = LocalDensity.current
    val borderWidthPx = with(density) { borderWidth.toPx() }
    val cornerRadiusPx = with(density) { cornerRadius.toPx() }

    // Log the isLoading state to debug
    Log.d("ProgressIndicator", "isLoading: $isLoading")

    // Animation progress (0f to 1f)
    val transition = rememberInfiniteTransition()
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (isLoading) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDurationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .size(size.dp)
            .background(backgroundColor)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable(onClick = onClick), // Add clickable modifier
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size.dp)) {
            val squareSize = size.dp.toPx()
            val halfStroke = borderWidthPx / 2f

            // Define the path for the rounded rectangle
            Path().apply {
                val left = halfStroke
                val top = halfStroke
                val right = squareSize - halfStroke
                val bottom = squareSize - halfStroke

                // Start at the top-left (after the corner radius)
                moveTo(left + cornerRadiusPx, top)

                // Top edge to top-right corner
                lineTo(right - cornerRadiusPx, top)
                arcTo(
                    rect = Rect(
                        left = right - 2 * cornerRadiusPx,
                        top = top,
                        right = right,
                        bottom = top + 2 * cornerRadiusPx
                    ),
                    startAngleDegrees = -90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Right edge to bottom-right corner
                lineTo(right, bottom - cornerRadiusPx)
                arcTo(
                    rect = Rect(
                        left = right - 2 * cornerRadiusPx,
                        top = bottom - 2 * cornerRadiusPx,
                        right = right,
                        bottom = bottom
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Bottom edge to bottom-left corner
                lineTo(left + cornerRadiusPx, bottom)
                arcTo(
                    rect = Rect(
                        left = left,
                        top = bottom - 2 * cornerRadiusPx,
                        right = left + 2 * cornerRadiusPx,
                        bottom = bottom
                    ),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Left edge to top-left corner
                lineTo(left, top + cornerRadiusPx)
                arcTo(
                    rect = Rect(
                        left = left,
                        top = top,
                        right = left + 2 * cornerRadiusPx,
                        bottom = top + 2 * cornerRadiusPx
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                close()
            }

            // Draw the static border with rounded corners
            drawRoundRect(
                color = if (isLoading) staticBorderLoadingColor else staticBorderColor,
                topLeft = Offset(halfStroke, halfStroke),
                size = Size(squareSize - borderWidthPx, squareSize - borderWidthPx),
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                style = Stroke(width = borderWidthPx)
            )

            // Draw the animated border if isLoading is true
            if (isLoading) {
                Log.d("ProgressIndicator", "Drawing animated border, progress: $progress")

                // Calculate the total perimeter of the rounded rectangle
                val straightLength = squareSize - borderWidthPx - 2 * cornerRadiusPx
                val cornerLength = 2 * Math.PI * cornerRadiusPx / 4 // One corner arc length
                val perimeter = 4 * straightLength + 4 * cornerLength
                val progressLength = perimeter * progress // Full perimeter animation

                // Define segment lengths
                val topLength = straightLength
                val topRightCornerLength = topLength + cornerLength
                val rightLength = topRightCornerLength + straightLength
                val bottomRightCornerLength = rightLength + cornerLength
                val bottomLength = bottomRightCornerLength + straightLength
                val bottomLeftCornerLength = bottomLength + cornerLength
                val leftLength = bottomLeftCornerLength + straightLength
                val topLeftCornerLength = leftLength + cornerLength

                // Create the animated path based on progress
                val animatedPath = Path().apply {
                    val left = halfStroke
                    val top = halfStroke
                    val right = squareSize - halfStroke
                    val bottom = squareSize - halfStroke

                    // Start at the top-left (after the corner radius)
                    moveTo(left + cornerRadiusPx, top)

                    if (progressLength > 0) {
                        // Top edge
                        if (progressLength <= topLength) {
                            lineTo((left + cornerRadiusPx + progressLength).toFloat(), top)
                        } else {
                            lineTo(right - cornerRadiusPx, top)
                        }

                        // Top-right corner
                        if (progressLength > topLength && progressLength <= topRightCornerLength) {
                            val cornerProgress = (progressLength - topLength) / cornerLength
                            arcTo(
                                rect = Rect(
                                    left = right - 2 * cornerRadiusPx,
                                    top = top,
                                    right = right,
                                    bottom = top + 2 * cornerRadiusPx
                                ),
                                startAngleDegrees = -90f,
                                sweepAngleDegrees = (90f * cornerProgress).toFloat(),
                                forceMoveTo = false
                            )
                        } else if (progressLength > topRightCornerLength) {
                            arcTo(
                                rect = Rect(
                                    left = right - 2 * cornerRadiusPx,
                                    top = top,
                                    right = right,
                                    bottom = top + 2 * cornerRadiusPx
                                ),
                                startAngleDegrees = -90f,
                                sweepAngleDegrees = 90f,
                                forceMoveTo = false
                            )
                        }

                        // Right edge
                        if (progressLength > topRightCornerLength && progressLength <= rightLength) {
                            val rightProgress = (progressLength - topRightCornerLength) / straightLength
                            lineTo(right,
                                (top + cornerRadiusPx + straightLength * rightProgress).toFloat()
                            )
                        } else if (progressLength > rightLength) {
                            lineTo(right, bottom - cornerRadiusPx)
                        }

                        // Bottom-right corner
                        if (progressLength > rightLength && progressLength <= bottomRightCornerLength) {
                            val cornerProgress = (progressLength - rightLength) / cornerLength
                            arcTo(
                                rect = Rect(
                                    left = right - 2 * cornerRadiusPx,
                                    top = bottom - 2 * cornerRadiusPx,
                                    right = right,
                                    bottom = bottom
                                ),
                                startAngleDegrees = 0f,
                                sweepAngleDegrees = (90f * cornerProgress).toFloat(),
                                forceMoveTo = false
                            )
                        } else if (progressLength > bottomRightCornerLength) {
                            arcTo(
                                rect = Rect(
                                    left = right - 2 * cornerRadiusPx,
                                    top = bottom - 2 * cornerRadiusPx,
                                    right = right,
                                    bottom = bottom
                                ),
                                startAngleDegrees = 0f,
                                sweepAngleDegrees = 90f,
                                forceMoveTo = false
                            )
                        }

                        // Bottom edge
                        if (progressLength > bottomRightCornerLength && progressLength <= bottomLength) {
                            val bottomProgress = (progressLength - bottomRightCornerLength) / straightLength
                            lineTo((right - cornerRadiusPx - straightLength * bottomProgress).toFloat(), bottom)
                        } else if (progressLength > bottomLength) {
                            lineTo(left + cornerRadiusPx, bottom)
                        }

                        // Bottom-left corner
                        if (progressLength > bottomLength && progressLength <= bottomLeftCornerLength) {
                            val cornerProgress = (progressLength - bottomLength) / cornerLength
                            arcTo(
                                rect = Rect(
                                    left = left,
                                    top = bottom - 2 * cornerRadiusPx,
                                    right = left + 2 * cornerRadiusPx,
                                    bottom = bottom
                                ),
                                startAngleDegrees = 90f,
                                sweepAngleDegrees = (90f * cornerProgress).toFloat(),
                                forceMoveTo = false
                            )
                        } else if (progressLength > bottomLeftCornerLength) {
                            arcTo(
                                rect = Rect(
                                    left = left,
                                    top = bottom - 2 * cornerRadiusPx,
                                    right = left + 2 * cornerRadiusPx,
                                    bottom = bottom
                                ),
                                startAngleDegrees = 90f,
                                sweepAngleDegrees = 90f,
                                forceMoveTo = false
                            )
                        }

                        // Left edge
                        if (progressLength > bottomLeftCornerLength && progressLength <= leftLength) {
                            val leftProgress = (progressLength - bottomLeftCornerLength) / straightLength
                            lineTo(left,
                                (bottom - cornerRadiusPx - straightLength * leftProgress).toFloat()
                            )
                        } else if (progressLength > leftLength) {
                            lineTo(left, top + cornerRadiusPx)
                        }

                        // Top-left corner
                        if (progressLength > leftLength && progressLength <= topLeftCornerLength) {
                            val cornerProgress = (progressLength - leftLength) / cornerLength
                            arcTo(
                                rect = Rect(
                                    left = left,
                                    top = top,
                                    right = left + 2 * cornerRadiusPx,
                                    bottom = top + 2 * cornerRadiusPx
                                ),
                                startAngleDegrees = 180f,
                                sweepAngleDegrees = (90f * cornerProgress).toFloat(),
                                forceMoveTo = false
                            )
                        } else if (progressLength > topLeftCornerLength) {
                            arcTo(
                                rect = Rect(
                                    left = left,
                                    top = top,
                                    right = left + 2 * cornerRadiusPx,
                                    bottom = top + 2 * cornerRadiusPx
                                ),
                                startAngleDegrees = 180f,
                                sweepAngleDegrees = 90f,
                                forceMoveTo = false
                            )
                        }
                    }
                }

                // Draw the animated path
                drawPath(
                    path = animatedPath,
                    color = animatedBorderColor,
                    style = Stroke(width = borderWidthPx)
                )
            }
        }

        // Custom icon with tint and size
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Custom Icon",
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}



@Composable
fun DashedLine(
    color: Color = Color.LightGray,
    thickness: Float = 1f,
    dashWidth: Float = 10f,
    dashGap: Float = 5f,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
) {
    Canvas(modifier = modifier) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = thickness,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(dashWidth, dashGap),
                phase = 0f
            )
        )
    }
}


@Composable
fun TopBar(
    onBackClick: () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = Modifier.fillMaxWidth().background(AppTheme.colors.topbar_bg).height(60.dp).padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        CustomCard(
            cardColor = AppTheme.colors.back_bg,
            cardCorner = 50,
            onClick = { onBackClick() },
            modifier = Modifier.size(35.dp)
        ) {
            Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                CustomIcon(
                    icon = R.drawable.ic_back_arrow,
                    modifier = Modifier.padding(end = 3.dp),
                    imageModifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(AppTheme.colors.onPrimary)
                )
            }
        }
        Text(
            title,
            style = AppTheme.textStyles.extraBold.largeTitle,
            color = AppTheme.colors.onPrimary,
            fontSize = 22.sp,
            modifier = Modifier.padding(start = 20.dp)
        )

    }
}

