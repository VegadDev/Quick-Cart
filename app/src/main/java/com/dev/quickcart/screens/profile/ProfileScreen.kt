package com.dev.quickcart.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
                    color = AppTheme.colors.titleText,
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
                    color = AppTheme.colors.titleText,
                    modifier = Modifier.padding(top = 20.dp)
                )

                Text(
                    text = uiState.userId,
                    style = AppTheme.textStyles.regular.regular,
                    color = AppTheme.colors.titleText,
                    modifier = Modifier.padding(top = 10.dp)
                )

            }


            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp),
                colors = CardDefaults.cardColors(AppTheme.colors.cardBackgroundColor),
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
                        color = AppTheme.colors.titleText,
                        modifier = Modifier.padding(top = 10.dp , start = 15.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp, start = 15.dp, end = 15.dp)
                            .clickable{ interActor.onEditProfileClick() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomIcon(
                            icon = R.drawable.ic_edit_profile,
                            modifier = Modifier,
                            imageModifier = Modifier.size(40.dp),
                            isCircle = false
                        )
                        Text(
                            "Edit Profile",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.titleText,
                            modifier = Modifier.padding(start = 20.dp),
                        )
                        Spacer(Modifier.weight(1f))
                        CustomIcon(
                            icon = R.drawable.ic_forward_arrow,
                            modifier = Modifier,
                            imageModifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(AppTheme.colors.titleText)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp, start = 15.dp, end = 15.dp)
                            .clickable{  },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomIcon(
                            icon = R.drawable.ic_myorders,
                            modifier = Modifier,
                            imageModifier = Modifier.size(40.dp),
                            isCircle = false
                        )
                        Text(
                            "My Orders",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.titleText,
                            modifier = Modifier.padding(start = 20.dp),
                        )
                        Spacer(Modifier.weight(1f))
                        CustomIcon(
                            icon = R.drawable.ic_forward_arrow,
                            modifier = Modifier,
                            imageModifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(AppTheme.colors.titleText)
                        )
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp, start = 15.dp, end = 15.dp)
                            .clickable{  },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomIcon(
                            icon = R.drawable.ic_addresses,
                            modifier = Modifier,
                            imageModifier = Modifier.size(40.dp),
                            isCircle = false
                        )
                        Text(
                            "Addresses",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.titleText,
                            modifier = Modifier.padding(start = 20.dp),
                        )
                        Spacer(Modifier.weight(1f))
                        CustomIcon(
                            icon = R.drawable.ic_forward_arrow,
                            modifier = Modifier,
                            imageModifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(AppTheme.colors.titleText)
                        )
                    }


                    var showDialog by remember {
                        mutableStateOf(false)
                    }

                    if (showDialog){
                        LogoutAlert(
                            onDismiss = {showDialog = false},
                            onConfirm = {
                                showDialog = false
                                interActor.onLogoutClick()
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp, start = 15.dp, end = 15.dp)
                            .clickable{ showDialog = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomIcon(
                            icon = if (isSystemInDarkTheme()) R.drawable.ic_logout else R.drawable.ic_logout_light,
                            modifier = Modifier,
                            imageModifier = Modifier.size(40.dp),
                            isCircle = false
                        )
                        Text(
                            "Logout",
                            style = AppTheme.textStyles.regular.regular,
                            color = AppTheme.colors.titleText,
                            modifier = Modifier.padding(start = 20.dp),
                        )
                        Spacer(Modifier.weight(1f))
                        CustomIcon(
                            icon = R.drawable.ic_forward_arrow,
                            modifier = Modifier,
                            imageModifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(AppTheme.colors.titleText)
                        )
                    }

                    Spacer(Modifier.weight(1f))


                    val isSelected by remember { mutableStateOf(false) }
                    Text(
                        "Appearance",
                        style = AppTheme.textStyles.regular.large,
                        color = AppTheme.colors.titleText,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Row {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            CustomCard(
                                cardColor = if (isSelected) AppTheme.colors.background else AppTheme.colors.cardBackgroundColor,
                                modifier = Modifier.height(60.dp).width(130.dp).padding(end = 10.dp),
                                border = if (isSelected) BorderStroke(2.dp , AppTheme.colors.lightGray) else BorderStroke(1.dp , AppTheme.colors.textColorLight)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CustomIcon(
                                        icon = R.drawable.ic_system,
                                        modifier = Modifier,
                                        imageModifier = Modifier.size(35.dp),
                                        isCircle = false,
                                        colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(AppTheme.colors.onPrimary) else null
                                    )
                                }
                            }
                            Text("System",
                                style = AppTheme.textStyles.regular.regular,
                                color = AppTheme.colors.titleText,
                                modifier = Modifier.padding(top = 5.dp , start = 35.dp),
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            CustomCard(
                                cardColor = if (isSelected) AppTheme.colors.onBackground else AppTheme.colors.cardBackgroundColor,
                                modifier = Modifier.height(60.dp).width(130.dp).padding(end = 10.dp),
                                border = if (isSelected) BorderStroke(2.dp , AppTheme.colors.lightGray) else BorderStroke(1.dp , AppTheme.colors.textColorLight)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CustomIcon(
                                        icon = R.drawable.ic_darkmode,
                                        modifier = Modifier,
                                        imageModifier = Modifier.size(35.dp),
                                        isCircle = false,
                                        colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(AppTheme.colors.onPrimary) else null
                                    )
                                }
                            }
                            Text("Dark",
                                style = AppTheme.textStyles.regular.regular,
                                color = AppTheme.colors.titleText,
                                modifier = Modifier.padding(top = 5.dp , start = 44.dp),
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            CustomCard(
                                cardColor = if (isSelected) AppTheme.colors.background else AppTheme.colors.cardBackgroundColor,
                                modifier = Modifier.height(60.dp).width(130.dp),
                                border = if (isSelected) BorderStroke(2.dp , AppTheme.colors.lightGray) else BorderStroke(1.dp , AppTheme.colors.textColorLight)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CustomIcon(
                                        icon = R.drawable.ic_lightmode,
                                        modifier = Modifier,
                                        imageModifier = Modifier.size(35.dp),
                                        isCircle = false,
                                        colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(AppTheme.colors.onPrimary) else null
                                    )
                                }
                            }
                            Text("Light",
                                style = AppTheme.textStyles.regular.regular,
                                color = AppTheme.colors.titleText,
                                modifier = Modifier.padding(top = 5.dp , start = 48.dp),
                            )
                        }

                    }


                }
            }
        }
    }
}








@Composable
fun LogoutAlert(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {

        CustomCard(
            cardColor = AppTheme.colors.cardBackgroundColor,
            modifier = Modifier.height(150.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Are you sure you want to Logout?",
                    style = AppTheme.textStyles.extraBold.large,
                    color = AppTheme.colors.titleText,
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
                    ) { Text(text = "Logout" , color = AppTheme.colors.onPrimary , style = AppTheme.textStyles.extraBold.regular) }
                }

            }


        }


    }



}
