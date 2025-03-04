package com.dev.quickcart.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.quickcart.R
import com.dev.quickcart.screens.common.AddressDropDownWidget
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.CustomSearchBar
import com.dev.quickcart.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(interActor: HomeInterActor = DefaultHomeInterActor, uiState: HomeUiState? = null) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(26.dp)
        ) {
            CustomIcon(
                icon = R.drawable.ic_user,
                modifier = Modifier.size(50.dp),
                imageModifier = Modifier
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .height(50.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    "Good Morning",
                    style = TextStyle(
                        color = AppTheme.colors.textColorLight,
                        fontSize = 16.sp
                    )
                )
                Text(
                    "Vegad Devdatt",
                    style = TextStyle(
                        color = AppTheme.colors.titleText,
                        fontSize = 19.sp
                    )
                )
            }

            Spacer(Modifier.weight(1f))

            AddressDropDownWidget()

        }


        CustomSearchBar(
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
            }
        )


        BannerCarousel(
            banners = listOf(
                Banner("1", R.drawable.banner_1, "Banner 1"),
                Banner("2", R.drawable.banner_2, "Banner 2"),
                Banner("2", R.drawable.banner_1, "Banner 2"),
                Banner("2", R.drawable.banner_1, "Banner 2"),
            ),
            onBannerClick = { banner ->
                // Handle banner click
            }
        )



        Row(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Categories 😋",
                style = AppTheme.textStyles.extraBold.large,
                color = AppTheme.colors.titleText,
                fontSize = 19.sp
            )
            Spacer(Modifier.weight(1f))
            Text(
                "See all",
                style = TextStyle(
                    color = AppTheme.colors.primary,
                    fontSize = 17.sp
                )
            )
        }




    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerCarousel(
    banners: List<Banner>,
    onBannerClick: (Banner) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(){
        banners.size
    }
    val scope = rememberCoroutineScope()

    // (Optional) Auto-scroll effect
    LaunchedEffect(key1 = pagerState.currentPage) {
        // Yahan aap kitne second mein page auto-scroll ho use define kar sakte ho
        delay(3000) // 3-second delay
        val nextPage = (pagerState.currentPage + 1) % banners.size
        pagerState.animateScrollToPage(nextPage)
    }

    HorizontalPager(
        beyondViewportPageCount = banners.size,
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) { page ->
        val banner = banners[page]
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onBannerClick(banner) }
        ) {
            Image(
                painter = painterResource(id = banner.imageRes),
                contentDescription = banner.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}



data class Banner(
    val id: String,
    val imageRes: Int,  // Drawable resource or you could use an image URL
    val title: String
)

