package com.dev.quickcart.screens.settings

interface SettingInterActor {

    fun gotoCategory()

}

object DefaultSettingInterActor: SettingInterActor {
    override fun gotoCategory() {}
}