package com.dev.quickcart.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.quickcart.ui.theme.AppTheme

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: Int? = null,
    buttonState: AppButtonState = AppButtonState.Active,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (buttonState) {
                AppButtonState.Active -> AppTheme.colors.primary
                AppButtonState.Secondary -> AppTheme.colors.onSecondary
                AppButtonState.Disabled -> AppTheme.colors.error
            }
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            if (icon != null) {
                Image(
                    painter = painterResource(id = icon), contentDescription = title,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Text(
                title,
                modifier = Modifier.padding(12.dp),
                textAlign = TextAlign.Center,
                //style = AppTheme.textStyles.extraBold.regular,
                color = when (buttonState) {
                    AppButtonState.Active -> AppTheme.colors.onPrimary
                    AppButtonState.Secondary -> AppTheme.colors.onSecondary
                    AppButtonState.Disabled -> AppTheme.colors.error
                }
            )
        }

    }
}


enum class AppButtonState {
    Active,
    Secondary,
    Disabled
}
@Preview
@Composable
private fun LoginPreview() {
    AppButton(title = "submit"){

    }
}