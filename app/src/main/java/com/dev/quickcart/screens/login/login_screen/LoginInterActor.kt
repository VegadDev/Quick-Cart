package com.dev.quickcart.screens.login.login_screen

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher


interface LoginInterActor {

    fun updateNumber(it: String)

    fun onGoogleSignInClick()
    fun handleGoogleSignInResult(result: ActivityResult)
    fun setGoogleSignInLauncher(launcher: ActivityResultLauncher<Intent>)

}