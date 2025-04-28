package com.dev.quickcart.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.dev.quickcart.R
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.ui.theme.AppTheme


@Composable
fun ProfileScreen(
    interActor: ProfileInterActor,
    uiState: ProfileUiState
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.login_bg_dark else R.drawable.login_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomCard(
                    cardColor = AppTheme.colors.onPrimary,
                    cardCorner = 50,
                    onClick = { interActor.onBackClick() },
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                ) {
                    CustomIcon(
                        icon = R.drawable.ic_back_arrow,
                        modifier = Modifier
                            .padding(9.dp)
                            .padding(end = 2.dp),
                        imageModifier = Modifier.size(25.dp)
                    )
                }
                Spacer(Modifier.weight(0.1f))
                Text(
                    "Profile",
                    style = AppTheme.textStyles.extraBold.largeTitle,
                    color = AppTheme.colors.white,
                    modifier = Modifier.padding(end = 170.dp, top = 20.dp)
                )

            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = uiState.userImage ?: R.drawable.ic_user, // Fallback to placeholder
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.ic_user),
                    error = painterResource(R.drawable.ic_user),
                    filterQuality = FilterQuality.Medium
                )

                Text(
                    text = uiState.userName,
                    style = AppTheme.textStyles.extraBold.large,
                    color = AppTheme.colors.white,
                    modifier = Modifier.padding(top = 20.dp)
                )

                Text(
                    text = uiState.userId,
                    style = AppTheme.textStyles.regular.regular,
                    color = AppTheme.colors.white,
                    modifier = Modifier.padding(top = 10.dp)
                )

            }


            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp),
                colors = CardDefaults.cardColors(AppTheme.colors.background),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                ) {
                    Text(
                        "Account Overview",
                        style = AppTheme.textStyles.regular.large,
                        color = AppTheme.colors.white,
                        modifier = Modifier.padding(top = 10.dp , start = 15.dp)
                    )

                    CustomCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        onClick = { interActor.onOrdersClick() }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomIcon(
                                icon = R.drawable.ic_myorders,
                                modifier = Modifier,
                                imageModifier = Modifier.size(40.dp),
                                isCircle = false
                            )
                            Column {
                                Text(
                                    "My Orders",
                                    style = AppTheme.textStyles.regular.large,
                                    color = AppTheme.colors.white,
                                    fontSize = 17.sp,
                                    modifier = Modifier.padding(start = 20.dp),
                                )
                                Text(
                                    "View your order history",
                                    style = AppTheme.textStyles.regular.regular,
                                    color = AppTheme.colors.lightGray,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(start = 20.dp),
                                )
                            }
                            Spacer(Modifier.weight(1f))
                            CustomIcon(
                                icon = R.drawable.ic_forward_arrow,
                                modifier = Modifier,
                                imageModifier = Modifier.size(20.dp),
                                colorFilter = ColorFilter.tint(AppTheme.colors.white)
                            )
                        }
                    }


                    CustomCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        onClick = { interActor.onAddressesClick() }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomIcon(
                                icon = R.drawable.ic_addresses,
                                modifier = Modifier,
                                imageModifier = Modifier.size(40.dp),
                                isCircle = false
                            )
                            Column {
                                Text(
                                    "Addresses",
                                    style = AppTheme.textStyles.regular.large,
                                    color = AppTheme.colors.white,
                                    fontSize = 17.sp,
                                    modifier = Modifier.padding(start = 20.dp),
                                )
                                Text(
                                    "Manage delivery addresses",
                                    style = AppTheme.textStyles.regular.regular,
                                    color = AppTheme.colors.lightGray,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(start = 20.dp),
                                )
                            }
                            Spacer(Modifier.weight(1f))
                            CustomIcon(
                                icon = R.drawable.ic_forward_arrow,
                                modifier = Modifier,
                                imageModifier = Modifier.size(20.dp),
                                colorFilter = ColorFilter.tint(AppTheme.colors.white)
                            )
                        }
                    }


                    CustomCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        onClick = { interActor.onAddressesClick() }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomIcon(
                                icon = R.drawable.ic_addresses,
                                modifier = Modifier,
                                imageModifier = Modifier.size(40.dp),
                                isCircle = false
                            )
                            Column {
                                Text(
                                    "Edit Profile",
                                    style = AppTheme.textStyles.regular.large,
                                    color = AppTheme.colors.white,
                                    fontSize = 17.sp,
                                    modifier = Modifier.padding(start = 20.dp),
                                )
                                Text(
                                    "Update your personal information",
                                    style = AppTheme.textStyles.regular.regular,
                                    color = AppTheme.colors.lightGray,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(start = 20.dp),
                                )
                            }
                            Spacer(Modifier.weight(1f))
                            CustomIcon(
                                icon = R.drawable.ic_forward_arrow,
                                modifier = Modifier,
                                imageModifier = Modifier.size(20.dp),
                                colorFilter = ColorFilter.tint(AppTheme.colors.white)
                            )
                        }
                    }


                    var showDialog by remember {
                        mutableStateOf(false)
                    }

                    if (showDialog){
                        CustomAlert(
                            onDismiss = {showDialog = false},
                            onConfirm = {
                                showDialog = false
                                interActor.onLogoutClick()
                            },
                            message = "Are you sure you want to Logout?",
                            title = "Logout"
                        )
                    }

                    Spacer(Modifier.weight(1f))
                    CustomCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        onClick = { showDialog = true }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomIcon(
                                icon = if (isSystemInDarkTheme()) R.drawable.ic_logout else R.drawable.ic_logout_light,
                                modifier = Modifier,
                                imageModifier = Modifier.size(35.dp),
                                isCircle = false
                            )
                            Text(
                                "Logout",
                                style = AppTheme.textStyles.regular.large,
                                color = AppTheme.colors.white,
                                modifier = Modifier.padding(start = 20.dp),
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                }
            }
        }
    }
}








@Composable
fun CustomAlert(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String = "",
    message: String = ""
) {

    Dialog(onDismissRequest = onDismiss) {

        CustomCard(
            cardColor = AppTheme.colors.cardBackgroundColor,
            modifier = Modifier.fillMaxWidth(),
            isClickable = false,
            cardCorner = 20
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = message,
                    style = AppTheme.textStyles.extraBold.large,
                    color = AppTheme.colors.white,
                    modifier = Modifier.padding(top = 15.dp)
                )
                Spacer(Modifier.size(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(AppTheme.colors.onPrimary),
                        border = BorderStroke(width = 1.dp, color = AppTheme.colors.primary),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text(text = "Cancel" , color = AppTheme.colors.textColorLight , style = AppTheme.textStyles.extraBold.regular) }

                    Spacer(modifier = Modifier.size(10.dp))

                    Button(
                        onClick = { onConfirm() },
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(AppTheme.colors.primary),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text(text = title , color = AppTheme.colors.onPrimary , style = AppTheme.textStyles.extraBold.regular) }
                }
            }
        }
    }
}
