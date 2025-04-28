package com.dev.quickcart.screens.home

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dev.quickcart.R
import com.dev.quickcart.data.model.Product
import com.dev.quickcart.screens.common.AddButton
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.CustomTextField
import com.dev.quickcart.screens.common.MyDropDown5
import com.dev.quickcart.screens.common.NewCard
import com.dev.quickcart.screens.common.ShimmerListItem
import com.dev.quickcart.ui.theme.AppTheme
import com.dev.quickcart.utils.displayQuantity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(interActor: HomeInterActor, uiState: HomeUiState) {


    val coroutineScope = rememberCoroutineScope()
    val viewModel: HomeViewModel = hiltViewModel()
    val loadingStates = viewModel.loadingStates.collectAsState().value

    val context = LocalContext.current

    val addressCategories = uiState.addresses.map { it.category }
    val initialCategory = addressCategories.firstOrNull() ?: "No Address"
    val selectedAddress by viewModel.networkRepository.getSelectedAddress().collectAsState()

    val shouldShowBottomSheet = selectedAddress == "" && uiState.addresses.isNotEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                elevation = CardDefaults.cardElevation(20.dp),
                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                colors = CardDefaults.cardColors(containerColor = AppTheme.colors.cardBackgroundColor)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CustomIcon(
                        icon = R.drawable.new_logo,
                        modifier = Modifier.padding(end = 8.dp),
                        imageModifier = Modifier.size(40.dp)
                    )

                    Text(
                        "Quick",
                        color = AppTheme.colors.primaryText,
                        style = AppTheme.textStyles.bold.largeTitle
                    )
                    Text(
                        "Cart",
                        color = AppTheme.colors.brandText,
                        style = AppTheme.textStyles.bold.largeTitle
                    )
                    Spacer(Modifier.weight(1f))
                    MyDropDown5(
                        items = uiState.addresses,
                        initialItem = selectedAddress ?: "",
                        onItemSelected = { selected ->
                            val cleanSelected = selected.trim()
                            Log.d("HomeScreen", "Selected Address: '$cleanSelected'")
                            interActor.setSelectedAddress(cleanSelected)
                        },
                        title = "Select Address",
                        showBottomSheet = shouldShowBottomSheet,
                        onNewAddress = { interActor.gotoAddAddress() },
                        modifier = Modifier.padding(end = 10.dp)
                    )

                    AsyncImage(
                        model = uiState.userImage ?: R.drawable.ic_user,
                        contentDescription = null,
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape)
                            .clickable { interActor.gotoProfile() },
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.ic_user),
                        error = painterResource(R.drawable.ic_user),
                        filterQuality = FilterQuality.Low
                    )

                }
            }

            Spacer(Modifier.height(7.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppTheme.colors.background)
            ) {

                item {

                    CustomTextField(
                        value = uiState.searchInput,
                        onValueChange = { interActor.updateSearchInput(it) },
                        onSearch = { },
                        hint = "Search...",
                        cornerShape = 40,
                        modifier = Modifier.padding(horizontal = 20.dp).padding(8.dp),
                        leadingIcon = {
                            CustomIcon(
                                icon = R.drawable.ic_search,
                                modifier = Modifier.padding(start = 8.dp),
                                imageModifier = Modifier.size(30.dp),
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search,
                        )
                    )

                    Spacer(Modifier.height(20.dp))
                    val banners = listOf(
                        R.drawable.banner_1,
                        R.drawable.banner_2,
                        R.drawable.banner_1
                    )

                    BannerCarousel(banners)



                    Row(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Best Selling 🔥",
                            style = AppTheme.textStyles.extraBold.largeTitle,
                            color = AppTheme.colors.white,
                            fontSize = 19.sp
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            "See all",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.primary,
                        )
                    }

                    if (uiState.isLoading) {
                        ShimmerListItem(uiState.isLoading)
                    } else {
                        LazyRow(
                            modifier = Modifier.padding(horizontal = 20.dp),
                        ) {
                            items(uiState.productList) { item ->
                                val isLoading = loadingStates[item.prodId.toString()] == true
                                ProductCard(
                                    product = item,
                                    onClick = {
                                        interActor.gotoProductPage(item.prodId)
                                    },
                                    isLoading = isLoading,
                                    addToCard = {
                                        coroutineScope.launch {
                                            uiState.isLoadingOnATC = false
                                            interActor.addToCart(item)
                                        }
                                    }
                                )
                            }
                        }
                    }


                    Row(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Vegetables",
                            style = AppTheme.textStyles.extraBold.largeTitle,
                            color = AppTheme.colors.white,
                            fontSize = 25.sp
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            "See all",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.primary,
                        )
                    }

                    val vegetablesProducts =
                        uiState.productList.filter { it.productCategory == "Vegetable" }


                    if (uiState.isLoading) {
                        ShimmerListItem(uiState.isLoading)
                    } else {
                        LazyRow(
                            modifier = Modifier.padding(horizontal = 20.dp),
                        ) {
                            items(vegetablesProducts) { item ->
                                val isLoading = loadingStates[item.prodId.toString()] == true
                                ProductCard(
                                    product = item,
                                    onClick = {
                                        interActor.gotoProductPage(item.prodId)
                                    },
                                    isLoading = isLoading,
                                    addToCard = {
                                        coroutineScope.launch {
                                            uiState.isLoadingOnATC = false
                                            interActor.addToCart(item)
                                        }
                                    }
                                )
                            }
                        }
                    }


                    Row(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Fruits",
                            style = AppTheme.textStyles.extraBold.largeTitle,
                            color = AppTheme.colors.white,
                            fontSize = 25.sp
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            "See all",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.primary,
                        )
                    }

                    val fruitsProducts =
                        uiState.productList.filter { it.productCategory == "Fruits" }

                    if (uiState.isLoading) {
                        ShimmerListItem(uiState.isLoading)
                    } else {
                        LazyRow(
                            modifier = Modifier.padding(horizontal = 20.dp),
                        ) {
                            items(fruitsProducts) { item ->
                                val isLoading = loadingStates[item.prodId.toString()] == true
                                ProductCard(
                                    product = item,
                                    onClick = {
                                        interActor.gotoProductPage(item.prodId)
                                    },
                                    isLoading = isLoading,
                                    addToCard = {
                                        coroutineScope.launch {
                                            interActor.addToCart(item)
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.size(100.dp))
                }


            }
        }
        val selectedCategory = uiState.selectedAddressCategory ?: initialCategory
        CustomCard(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 20.dp),
            cardColor = AppTheme.colors.primary,
            cardCorner = 17,
            cardElevation = 30,
            onClick = {
                if (selectedCategory != null) {
                    val cleanSelected = selectedCategory.trim()
                    Log.d("HomeScreen", "Navigating to Cart with: '$cleanSelected'")
                    interActor.gotoCart(cleanSelected)
                    Log.d("HomeScreen", "Navigating to Cart with: $selectedCategory")
                } else {
                    uiState.copy(showBottomSheet = mutableStateOf(true))
                    Toast.makeText(context, "Please select an address", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Row(
                modifier = Modifier.padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Cart",
                    style = AppTheme.textStyles.extraBold.largeTitle,
                    color = AppTheme.colors.onPrimary,
                    modifier = Modifier.padding(end = 8.dp),
                )
                val cartCount = uiState.cartItems.sumOf { it.quantity }
                CustomCard(
                    cardColor = AppTheme.colors.darkGreen,
                    isClickable = false
                ) {
                    Text(
                        cartCount.toString(),
                        style = AppTheme.textStyles.extraBold.regular,
                        color = AppTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }
            }

        }

    }
}









@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onClick: () -> Unit = {},
    addToCard: () -> Unit = {},
    isLoading: Boolean = false
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    if (product.prodImage == null) {
        NewCard(
            modifier = modifier,
            cardWidth = 160,
            cardHeight = 230,
            cardCorner = 15,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomIcon(
                    icon = R.drawable.no_image,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                    imageModifier = Modifier.size(100.dp)
                )
            }
        }
    } else {
        NewCard(
            modifier = modifier.padding(end = 17.dp),
            cardWidth = 160,
            cardHeight = 230,
            cardCorner = 15,
            onClick = {
                coroutineScope.launch {
                    onClick()
                    delay(2000)
                }
            }
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp)
            ) {

                Text(
                    text = product.prodName,
                    style = AppTheme.textStyles.bold.large,
                    color = AppTheme.colors.white
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    product.prodImage.let { blob ->
                        val bitmap = remember(blob) { // Cache bitmap decoding
                            BitmapFactory.decodeByteArray(blob.toBytes(), 0, blob.toBytes().size)
                        }
                        bitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "Product Image",
                                filterQuality = FilterQuality.Low,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = displayQuantity(product),
                        style = AppTheme.textStyles.regular.regular,
                        color = AppTheme.colors.textColorLight,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    CustomCard(
                        cardColor = AppTheme.colors.primary,
                        modifier = Modifier,
                        cardCorner = 30,
                        onClick = {
                            coroutineScope.launch {
                                addToCard()
                            }
                            Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AddButton(
                                modifier = Modifier.size(34.dp),
                                color = AppTheme.colors.cardBackgroundColor,
                                isLoading = isLoading,
                                onAddClick = {
                                    coroutineScope.launch {
                                        addToCard()
                                    }
                                    Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show()
                                }
                            )
                            Text(
                                "₹ ${product.prodPrice}",
                                style = AppTheme.textStyles.bold.large,
                                color = AppTheme.colors.onPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                        }
                    }
                }

            }

        }
    }


}


@Composable
fun BannerCarousel(banners: List<Int>) {

    val pagerState = rememberPagerState(pageCount = { Int.MAX_VALUE })
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll logic
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // Change slide every 3 seconds
            coroutineScope.launch {
                val nextPage = (pagerState.currentPage + 1) % banners.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .height(180.dp)
        ) { page ->
            val actualPage = page % banners.size // Get the correct banner index
            BannerItem(image = painterResource(id = banners[actualPage]))
        }
    }
}

@Composable
fun BannerItem(image: Painter) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}


