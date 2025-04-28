package com.dev.quickcart.screens.login.intro

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.dev.quickcart.R
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.MyButton
import com.dev.quickcart.ui.theme.AppTheme

@Composable
fun IntroScreen(interActor: IntroInterActor) {


    Column(
        modifier = Modifier.fillMaxSize()
    ) {


        Box(
            modifier = Modifier.fillMaxSize(),
        ) {


            CustomIcon(
                icon = if (isSystemInDarkTheme()) R.drawable.intro_bg else R.drawable.intro_bg_light,
                modifier = Modifier.fillMaxSize(),
                imageModifier = Modifier.fillMaxSize(),
                isCircle = false,
                contentScale = ContentScale.FillBounds
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                CustomIcon(
                    icon = if (isSystemInDarkTheme()) R.drawable.app_logo else R.drawable.app_logo_light,
                    modifier = Modifier
                        .padding(top = 100.dp),
                    imageModifier = Modifier.size(90.dp)
                )
                Spacer(Modifier.size(30.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Get your groceries",
                        style = AppTheme.textStyles.extraBold.largeTitle,
                        color = AppTheme.colors.white
                    )
                    Text("delivered to your home",
                        style = AppTheme.textStyles.extraBold.largeTitle,
                        color = AppTheme.colors.white
                    )
                    Text("The best delivery app in town for",
                        style = AppTheme.textStyles.medium.small,
                        color = AppTheme.colors.textColorLight,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    Text("delivering your daily fresh groceries",
                        style = AppTheme.textStyles.medium.small,
                        color = AppTheme.colors.textColorLight
                    )
                }

                Spacer(Modifier.size(50.dp))

                MyButton(
                    "Shop Now",
                    onClick = { interActor.gotoLogin()},
                    textModifier = Modifier.padding(horizontal = 18.dp)
                )


            }



        }
    }


}