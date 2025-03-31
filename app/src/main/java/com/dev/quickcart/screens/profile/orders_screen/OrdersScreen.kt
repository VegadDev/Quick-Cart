package com.dev.quickcart.screens.profile.orders_screen

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dev.quickcart.R
import com.dev.quickcart.screens.common.AddButton
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.NewCard
import com.dev.quickcart.ui.theme.AppTheme
import com.dev.quickcart.utils.displayQuantity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OrderScreen(interActor: OrdersInterActor, uiState: OrdersUiState) {


    NewCard(
        modifier = Modifier.padding(end = 17.dp),
        cardWidth = 160,
        cardHeight = 230,
        cardCorner = 15,
        onClick = {
//            coroutineScope.launch {
//                onClick()
//                delay(2000)
//            }
        }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp)
        ) {

            Text(
                text = "Banana Apple",
                style = AppTheme.textStyles.bold.large,
                color = AppTheme.colors.titleText
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp)
                    .padding(top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
//                product.prodImage.let { blob ->
//                    val bitmap = remember(blob) { // Cache bitmap decoding
//                        BitmapFactory.decodeByteArray(blob.toBytes(), 0, blob.toBytes().size)
//                    }
//                    bitmap?.let {
                        Image(
                            painter = painterResource(R.drawable.no_image),
                            contentDescription = "Product Image",
                            //filterQuality = FilterQuality.Low,
                            modifier = Modifier.fillMaxSize(),
                        )
                    //}
                //}
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "1kg, Price",
                    style = AppTheme.textStyles.regular.regular,
                    color = AppTheme.colors.textColorLight,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CustomCard(
                    cardColor = AppTheme.colors.primary,
                    modifier = Modifier.fillMaxWidth(),
                    cardCorner = 30
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AddButton(
                            modifier = Modifier.size(34.dp),
                            color = AppTheme.colors.cardBackgroundColor
                        )
                        Text(
                            "₹ 200",
                            style = AppTheme.textStyles.bold.large,
                            color = AppTheme.colors.cardBackgroundColor,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
            }

        }

    }

}