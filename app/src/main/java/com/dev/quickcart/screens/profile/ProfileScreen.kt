package com.dev.quickcart.screens.profile

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun ProfileScreen(interActor: ProfileInterActor) {

    Text("Profile Screen")
    Button(
        onClick = { interActor.gotoHome() }
    ) { }
}