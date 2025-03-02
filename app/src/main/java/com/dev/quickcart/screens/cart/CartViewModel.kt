package com.dev.quickcart.screens.cart

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CartViewModel
@Inject
constructor(

) : ViewModel() {

    val interActor = object : CartInterActor {

    }

}