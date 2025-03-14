package com.dev.quickcart.screens.login.login_screen

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dev.quickcart.R
import com.dev.quickcart.screens.common.MyButton
import com.dev.quickcart.screens.common.CustomCard
import com.dev.quickcart.screens.common.CustomIcon
import com.dev.quickcart.screens.common.CustomTextField
import com.dev.quickcart.ui.theme.AppTheme




@Composable
fun LoginScreen(interActor: LoginInterActor, uiState: LoginUiState) {


    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("LoginScreen", "Result received: resultCode=${result.resultCode}, data=${result.data}")
        interActor.handleGoogleSignInResult(result)
    }

    // Use a stable key to ensure launcher persists across recompositions
    LaunchedEffect(googleSignInLauncher) {
        Log.d("LoginScreen", "Setting launcher: $googleSignInLauncher")
        interActor.setGoogleSignInLauncher(googleSignInLauncher)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        CustomIcon(
            icon = if (isSystemInDarkTheme()) R.drawable.login_bg_dark else R.drawable.login_bg,
            modifier = Modifier.fillMaxSize(),
            imageModifier = Modifier.fillMaxSize(),
            isCircle = false,
            contentScale = ContentScale.FillBounds
        )


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            CustomIcon(
                icon = if (isSystemInDarkTheme()) R.drawable.app_logo else R.drawable.app_logo_light,
                modifier = Modifier
                    .padding(top = 100.dp),
                imageModifier = Modifier.size(90.dp)
            )
            Spacer(Modifier.size(30.dp))
            Text(
                "Enter your mobile",
                color = AppTheme.colors.titleText,
                style = AppTheme.textStyles.bold.largeTitle
            )
            Text(
                "number",
                color = AppTheme.colors.titleText,
                style = AppTheme.textStyles.bold.largeTitle
            )
            Spacer(Modifier.size(10.dp))
            Text(
                "We will send you a verification code",
                color = AppTheme.colors.lightGray,
                style = AppTheme.textStyles.bold.regular
            )
            Spacer(Modifier.size(30.dp))
            CustomTextField(
                value = (uiState.numberInput),
                onValueChange = {
                    val sanitizedValue =
                        it.filter { it.isDigit() } // Keep only numbers (remove spaces)
                    if (sanitizedValue.length <= 10) { // Restrict to 10 digits (excluding spaces)
                        interActor.updateNumber(sanitizedValue)
                    }
                },
                modifier = Modifier.padding(horizontal = 70.dp),
                onSearch = { },
                textFieldColors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                hint = "(000) 000-00-00",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Go,
                    keyboardType = KeyboardType.Number
                ),
                prefix = {
                    Text(
                        "+91  ",
                        style = AppTheme.textStyles.extraBold.large,
                        color = AppTheme.colors.titleText,
                        modifier = Modifier.padding(start = 13.dp)
                    )
                },
                textStyle = AppTheme.textStyles.bold.large
            )
            Spacer(Modifier.size(50.dp))

            var showOtpDialog by remember { mutableStateOf(false) }

            MyButton(
                "Continue",
                modifier = Modifier.fillMaxWidth(0.85f),
                onClick = { showOtpDialog = true }
            )


            Text(
                "Or connect with",
                color = AppTheme.colors.lightGray,
                style = AppTheme.textStyles.bold.regular,
                modifier = Modifier.padding(vertical = 25.dp)
            )
            CustomCard(
                onClick = {
                    interActor.onGoogleSignInClick()
                    Log.d("LoginScreen", "Google Sign-In button clicked")
                          },
                cardCorner = 40,
                cardColor = AppTheme.colors.googleButtonColor,
                modifier = Modifier.fillMaxWidth(0.85f).height(56.dp),
            ){
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomIcon(
                        icon = R.drawable.ic_google,
                        imageModifier = Modifier.size(25.dp),
                        modifier = Modifier.padding(start = 60.dp)
                    )
                    Spacer(Modifier.size(10.dp))
                    Text(
                        "Continue with Google",
                        color = AppTheme.colors.onPrimary,
                        style = AppTheme.textStyles.bold.large,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Log.d("LoginScreen", "Showing loading indicator")
            }
            uiState.error?.let { error ->
                Text(
                    text = "Error: $error",
                    color = AppTheme.colors.error,
                    style = AppTheme.textStyles.bold.regular,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )
                Log.e("LoginScreen", "Error displayed: $error")
            }
            uiState.userEmail?.let { email ->
                Text(
                    text = "Signed in as: $email",
                    color = AppTheme.colors.titleText,
                    style = AppTheme.textStyles.bold.regular,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )
                Log.d("LoginScreen", "User email displayed: $email")
            }

        }

    }


}



//            if (showOtpDialog) {
//                OtpPopup(
//                    onDismiss = { showOtpDialog = false },
//                    onOtpVerify = {
//                        showOtpDialog = false
//                        interActor.gotoHomeScreen()
//                    }
//                )
//            }


@Composable
fun OtpPopup(onDismiss: () -> Unit, onOtpVerify: () -> Unit) {
    var otpValue by remember { mutableStateOf(List(4) { "" }) }
    val focusRequesters = List(4) { FocusRequester() }

    Dialog(onDismissRequest = onDismiss) {
        CustomCard(
            cardCorner = 20,
            isClickable = false,
            modifier = Modifier.padding(6.dp).fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Enter OTP",
                    style = AppTheme.textStyles.bold.largeTitle,
                    color = AppTheme.colors.titleText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "A verification code has been sent to (205) 555-0100",
                    color = AppTheme.colors.lightGray,
                    style = AppTheme.textStyles.bold.regular,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                // OTP Input Fields
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    otpValue.forEachIndexed { index, text ->

                        TextField(
                            value = text,
                            onValueChange = { newValue ->
                                if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                    val newOtp = otpValue.toMutableList()
                                    newOtp[index] = newValue
                                    otpValue = newOtp

                                    if (newValue.isNotEmpty() && index < 3) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .focusRequester(focusRequesters[index])
                                .border(1.dp, AppTheme.colors.primary, RoundedCornerShape(8.dp))
                                .background(Color.White, RoundedCornerShape(8.dp)),
                            textStyle = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center , fontWeight = FontWeight.ExtraBold),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = AppTheme.colors.cardBackgroundColor,
                                unfocusedContainerColor = AppTheme.colors.cardBackgroundColor,
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                MyButton(
                    "Verify",
                    onClick = { onOtpVerify() },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Didn't receive the code? Resend (30s)",
                    color = AppTheme.colors.primary,
                    style = AppTheme.textStyles.regular.small,
                    modifier = Modifier.clickable { /* Resend OTP Logic */ }
                )
            }
        }
    }
}




