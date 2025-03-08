package com.dev.quickcart.screens.home

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.quickcart.R
import com.dev.quickcart.screens.common.AddressDropDownWidget
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.MyButton
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.CustomTextField
import com.dev.quickcart.screens.common.NewCard
import com.dev.quickcart.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(interActor: HomeInterActor = DefaultHomeInterActor, uiState: HomeUiState? = null) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(26.dp)
        ) {
            CustomIcon(
                icon = R.drawable.ic_user,
                modifier = Modifier.clickable{ interActor.gotoProfile()},
                imageModifier = Modifier.size(50.dp)
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
                    "Vegad Devdatt",
                    style = AppTheme.textStyles.bold.large,
                    color = AppTheme.colors.titleText,

                    )
            }

            Spacer(Modifier.weight(1f))

            AddressDropDownWidget()

        }


        CustomTextField(
            value = uiState?.searchInput ?: "Error",
            onValueChange = { interActor.updateSearchInput(it) },
            onSearch = { },
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

        LazyRow(
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            item {
                ProductCard(
                    productName = "Banana",
                    productPrice = "50",
                    productImage = R.drawable.prod_banana,
                )
                Spacer(Modifier.size(15.dp))
                ProductCard(
                    productPrice = "90"
                )
                Spacer(Modifier.size(15.dp))
                ProductCard(
                    productName = "Gajar",
                    productPrice = "70",
                    productImage = R.drawable.prod_gajar,
                )
            }

        }


    }

}

@Preview(showBackground = true)
@Composable
private fun View() {
    Column(Modifier.background(Color.Black)) {
        ProductCard()
    }

}


@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    productImage: Int? = null,
    productName: String = "",
    productPrice: String = "",
    onClick: () -> Unit = {},
    AddToCard: () -> Unit = {},
) {

    if (productImage == null) {
        NewCard(
            modifier = modifier,
            cardWidth = 160,
            cardHeight = 275,
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
    }

    else {
        NewCard(
            modifier = modifier,
            cardWidth = 160,
            cardHeight = 275,
            cardCorner = 15,
            onClick = { onClick() }
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp)
            ) {

                Text(
                    text = productName,
                    style = AppTheme.textStyles.bold.large,
                    color = AppTheme.colors.titleText
                )
                CustomIcon(
                    icon = productImage,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                    imageModifier = Modifier.size(100.dp)
                )

                Column {
                    Text(
                        "₹ $productPrice",
                        style = AppTheme.textStyles.bold.large,
                        color = AppTheme.colors.primary
                    )
                    Text(
                        "1kg - price/kg",
                        style = AppTheme.textStyles.regular.regular,
                        color = AppTheme.colors.textColorLight
                    )

                    CustomCard(
                        cardColor = AppTheme.colors.primary,
                        cardCorner = 20,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(top = 10.dp),
                        onClick = { AddToCard() }
                    ) {
                        Row(Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center) {
                            Text("Add to cart",
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


