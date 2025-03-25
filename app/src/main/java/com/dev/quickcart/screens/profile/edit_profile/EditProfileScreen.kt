package com.dev.quickcart.screens.profile.edit_profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dev.quickcart.screens.common.CustomTextField
import com.dev.quickcart.ui.theme.AppTheme

@Composable
fun EditProfileScreen(interActor: EditProfileInterActor, uiState: EditProfileUiState) {


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            "Update UserName",
            style = AppTheme.textStyles.bold.large,
            color = AppTheme.colors.titleText
        )
        Spacer(Modifier.size(10.dp))
        CustomTextField(
            value = uiState.editName,
            onValueChange = { interActor.updateUserName(it) },
            onSearch = {},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            error = uiState.editNameError,
            hint = "Type Here"
        )
        Spacer(Modifier.size(20.dp))

        Text(
            "Update Email",
            style = AppTheme.textStyles.bold.large,
            color = AppTheme.colors.titleText
        )
        Spacer(Modifier.size(10.dp))
        CustomTextField(
            value = uiState.editEmail,
            onValueChange = { interActor.updateEmail(it) },
            onSearch = {},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            error = uiState.editEmailError,
            hint = "Type Here"
        )
        Spacer(Modifier.size(20.dp))

        Text(
            "Update Mobile Number",
            style = AppTheme.textStyles.bold.large,
            color = AppTheme.colors.titleText
        )
        Spacer(Modifier.size(10.dp))
        CustomTextField(
            value = uiState.mobileNumber,
            onValueChange = { interActor.updateMobileNumber(it) },
            onSearch = {},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            error = uiState.mobileNumberError,
            hint = "Type Here"
        )


    }


}