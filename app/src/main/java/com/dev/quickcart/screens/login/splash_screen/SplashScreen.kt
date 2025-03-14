package com.dev.quickcart.screens.login.splash_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.quickcart.R
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.MyButton
import com.dev.quickcart.ui.theme.AppTheme

@Composable
fun SplashScreen(
    uiState: SplashUiState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
    ) {
        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            CustomIcon(
                icon = if (isSystemInDarkTheme()) R.drawable.app_logo else R.drawable.app_logo_light,
                modifier = Modifier.size(100.dp),
                imageModifier = Modifier.size(100.dp)
            )
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    color = AppTheme.colors.primary
                )
            }
        }

        Text(
            text = "Quick Cart",
            style = AppTheme.textStyles.extraBold.largeTitle,
            color = AppTheme.colors.titleText,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 180.dp)
        )

        // Internet Off UI
        if (uiState.noInternet) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Turn on your internet",
                    color = AppTheme.colors.error,
                    style = AppTheme.textStyles.bold.large,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                val viewModel: SplashScreenViewModel = hiltViewModel()
                MyButton(
                    text = "Retry",
                    modifier = Modifier.fillMaxWidth(0.5f),
                    onClick = {
                        viewModel.checkInternetAndSignInStatus()
                    }
                )
            }
        }
    }
}