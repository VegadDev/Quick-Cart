package com.dev.quickcart.screens.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingScreen(interActor: SettingInterActor = DefaultSettingInterActor) {

    Row(
        Modifier.fillMaxWidth()
    ) {
        Button(
            modifier = Modifier.height(50.dp),
            onClick = { interActor.gotoCategory() }
        ) { }
    }

}