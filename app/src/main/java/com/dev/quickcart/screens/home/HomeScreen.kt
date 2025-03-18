package com.dev.quickcart.screens.home

import android.graphics.BitmapFactory
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dev.quickcart.R
import com.dev.quickcart.data.model.Product
import com.dev.quickcart.screens.addProduct.stringToByteArray
import com.dev.quickcart.screens.common.MyDropDown
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.CustomTextField
import com.dev.quickcart.screens.common.NewCard
import com.dev.quickcart.screens.common.ShimmerListItem
import com.dev.quickcart.screens.common.shimmerEffect
import com.dev.quickcart.ui.theme.AppTheme
import com.dev.quickcart.utils.displayQuantity
import com.dev.quickcart.utils.uriToBlob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(interActor: HomeInterActor, uiState: HomeUiState) {



    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.background)
        ) {

            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(26.dp)
                ) {
                    AsyncImage(
                        model = uiState.userImage ?: R.drawable.ic_user,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable{ interActor.gotoProfile() },
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.ic_user),
                        error = painterResource(R.drawable.ic_user),
                        filterQuality = FilterQuality.Medium
                    )

                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .height(50.dp),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            "Good Morning",
                            style = AppTheme.textStyles.bold.regular,
                            color = AppTheme.colors.textColorLight,
                        )
                        Text(
                            text = uiState.userName,
                            style = AppTheme.textStyles.bold.large,
                            color = AppTheme.colors.titleText
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    MyDropDown(
                        items = listOf("My Flat", "Home", "Office", "Other"),
                        title = "Select Address"
                    )

                }


                CustomTextField(
                    value = uiState.searchInput,
                    onValueChange = { interActor.updateSearchInput(it) },
                    onSearch = { },
                    hint = "Search...",
                    cornerShape = 40,
                    modifier = Modifier.padding(horizontal = 20.dp),
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
                        color = AppTheme.colors.titleText,
                        fontSize = 19.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "See all",
                        style = AppTheme.textStyles.regular.regular,
                        color = AppTheme.colors.primary,
                    )
                }

                if (uiState.isLoading){
                    ShimmerListItem(uiState.isLoading)
                }
                else {
                    LazyRow(
                        modifier = Modifier.padding(horizontal = 20.dp),
                    ) {
                        items(uiState.productList) { item ->
                            ProductCard(
                                product = item,
                                onClick = {
                                    interActor.gotoProductPage(item.prodId)
                                }
                            )
                        }
                    }
                }


                Row(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "Vegetables 🥕",
                        style = AppTheme.textStyles.extraBold.largeTitle,
                        color = AppTheme.colors.titleText,
                        fontSize = 19.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "See all",
                        style = AppTheme.textStyles.regular.regular,
                        color = AppTheme.colors.primary,
                    )
                }

                val vegetablesProducts = uiState.productList.filter { it.productCategory == "Vegetable" }


                if (uiState.isLoading){
                    ShimmerListItem(uiState.isLoading)
                }
                else {
                    LazyRow(
                        modifier = Modifier.padding(horizontal = 20.dp),
                    ) {
                        items(vegetablesProducts) { item ->
                            ProductCard(
                                product = item,
                                onClick = {
                                        interActor.gotoProductPage(item.prodId)
                                }
                            )
                        }
                    }
                }


                Row(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "Fruits 🍎",
                        style = AppTheme.textStyles.extraBold.largeTitle,
                        color = AppTheme.colors.titleText,
                        fontSize = 19.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        "See all",
                        style = AppTheme.textStyles.regular.regular,
                        color = AppTheme.colors.primary,
                    )
                }

                val fruitsProducts = uiState.productList.filter { it.productCategory == "Fruits" }


                if (uiState.isLoading){
                    ShimmerListItem(uiState.isLoading)
                }
                else {
                    LazyRow(
                        modifier = Modifier.padding(horizontal = 20.dp),
                    ) {
                        items(fruitsProducts) { item ->
                            ProductCard(
                                product = item,
                                onClick = {
                                    interActor.gotoProductPage(item.prodId)
                                }
                            )
                        }
                    }
                }


            }


        }

        CustomCard(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 20.dp),
            cardColor = AppTheme.colors.primary,
            cardCorner = 17,
            cardElevation = 19,
            onClick = { interActor.gotoCart() }
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
                CustomCard(
                    cardColor = AppTheme.colors.darkGreen,
                    isClickable = false
                ) {
                    Text(
                        "1",
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
) {

    val coroutineScope = rememberCoroutineScope()

    if (product.prodImage == null) {
        NewCard(
            modifier = modifier,
            cardWidth = 160,
            cardHeight = 280,
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
            cardHeight = 275,
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
                    color = AppTheme.colors.titleText
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    val byteArray = product.prodImage.toBytes()
                    val bitmap = byteArray.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            filterQuality = FilterQuality.Low,
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        Text("Failed to load image")
                    }
                }



                Column {
                    Text(
                        "₹ ${product.prodPrice}",
                        style = AppTheme.textStyles.bold.large,
                        color = AppTheme.colors.primary
                    )
                    Text(
                        text = displayQuantity(product),
                        style = AppTheme.textStyles.regular.regular,
                        color = AppTheme.colors.textColorLight
                    )

                    CustomCard(
                        cardColor = AppTheme.colors.primary,
                        cardCorner = 20,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(top = 10.dp),
                        onClick = { addToCard() }
                    ) {
                        Row(
                            Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Add to cart",
                                style = AppTheme.textStyles.bold.regular
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
                .fillMaxWidth()
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


