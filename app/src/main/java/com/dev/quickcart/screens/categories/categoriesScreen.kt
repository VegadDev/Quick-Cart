package com.dev.quickcart.screens.categories

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    interActor: CategoriesInterActor = DefaultCategoriesInterActor
) {

    Text("Categories Screen")


}