package com.dev.quickcart.screens.profile

interface ProfileInterActor {
    fun gotoHome()
}


object DefaultProfileInterActor: ProfileInterActor {
    override fun gotoHome() {}

}