package com.dev.quickcart.screens.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(interActor: HomeInterActor) {

    Row(
        modifier = Modifier.height(50.dp)
    ) {
        Text("Home Screen")

        Button(
            onClick = { interActor.gotoProfile() },
            modifier = Modifier.height(50.dp)
        ) { }
    }



}