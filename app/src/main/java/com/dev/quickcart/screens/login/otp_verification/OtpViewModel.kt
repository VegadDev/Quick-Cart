package com.dev.quickcart.screens.login.otp_verification

import androidx.lifecycle.ViewModel
import com.dev.quickcart.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class OtpViewModel
@Inject
constructor(
    private val navigator: Navigator
) : ViewModel() {

    val interActor = object : OtpInterActor {


    }
}