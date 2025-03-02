package com.dev.quickcart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NavBar(
    defaultSelectedIndex: Int = 0,
    list: List<Int>
) {

    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
            .height(56.dp),
        color = Color.White
    ) {

        var selectedIndex by remember { mutableIntStateOf(defaultSelectedIndex) }


        Row(Modifier.fillMaxSize()) {

            list.forEachIndexed { index, icon ->

                Box(
                    Modifier.fillMaxHeight().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(){}

                }

            }

        }


    }


}