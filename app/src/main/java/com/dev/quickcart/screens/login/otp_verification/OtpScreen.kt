package com.dev.quickcart.screens.login.otp_verification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpScreen(interActor: OtpInterActor) {


    var otpValue by remember { mutableStateOf(List(4) { "" }) }
    val focusManager = LocalFocusManager.current
    val focusRequesters = List(4) { FocusRequester() }

    OtpVerificationScreen()

}


@Composable
fun OtpVerificationScreen() {
    var otpValue by remember { mutableStateOf(List(4) { "" }) }
    val focusManager = LocalFocusManager.current
    val focusRequesters = List(4) { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Enter Verification Code",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "We have sent SMS to:\n 01XXXXXXXXXX",
            style = TextStyle(fontSize = 16.sp, color = Color.Gray),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            otpValue.forEachIndexed { index, text ->
                TextField(
                    value = text,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                            val newOtpValue = otpValue.toMutableList()
                            newOtpValue[index] = newValue
                            otpValue = newOtpValue

                            // Move focus to next field
                            if (newValue.isNotEmpty() && index < 3) {
                                focusRequesters[index + 1].requestFocus()
                            }
                        }
                    },
                    modifier = Modifier
                        .size(60.dp)
                        .focusRequester(focusRequesters[index])
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .background(Color.White, RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (index == 3) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { if (index < 3) focusRequesters[index + 1].requestFocus() },
                        onDone = { focusManager.clearFocus() }
                    ),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Resend OTP",
                style = TextStyle(color = Color(0xFFFF6F00), fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable { /* Resend OTP Logic */ }
            )
            Text(
                text = "Change Phone Number",
                style = TextStyle(color = Color.Gray),
                modifier = Modifier.clickable { /* Change Number Logic */ }
            )
        }
    }
}
