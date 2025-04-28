package com.dev.quickcart.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dev.quickcart.R
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.CustomTextField
import com.dev.quickcart.ui.theme.AppTheme

@Composable
fun SettingScreen(interActor: SettingInterActor = DefaultSettingInterActor) {


    Column(
        modifier = Modifier
    ) {
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
                    color = AppTheme.colors.darkGreen,
                    style = AppTheme.textStyles.bold.largeTitle
                )
                Text(
                    "Cart",
                    color = AppTheme.colors.primary,
                    style = AppTheme.textStyles.bold.largeTitle
                )
                Spacer(Modifier.weight(1f))


                CustomIcon(
                    icon = R.drawable.people,
                    modifier = Modifier.padding(start = 15.dp),
                    imageModifier = Modifier.size(30.dp),
                    isCircle = false
                )

            }
        }


        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
        ) {

            CustomTextField(
                value = "asd",
                onValueChange = {},
                onSearch = {}
            )

            CustomIcon(
                icon = R.drawable.banner_1,
                modifier = Modifier.padding(top = 17.dp),
                imageModifier = Modifier.fillMaxWidth().height(180.dp),
                isCircle = false,
            )

            Text(
                "Best Sellers",
                color = AppTheme.colors.white,
                style = AppTheme.textStyles.bold.largeTitle
            )




        }
    }


}