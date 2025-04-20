package com.dev.quickcart.screens.categories

import androidx.lifecycle.ViewModel
import com.dev.quickcart.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CategoriesViewModel
@Inject
constructor(
    private val navigator: Navigator
) : ViewModel() {

    val interActor = object : CategoriesInterActor {


    }
}