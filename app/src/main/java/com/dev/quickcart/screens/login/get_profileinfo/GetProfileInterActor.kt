package com.dev.quickcart.screens.login.get_profileinfo

interface GetProfileInterActor{

    fun updateUserName(it: String)
    fun updateMobileNumber(it: String)
    fun updateHouseAddress(it: String)
    fun updateAreaAddress(it: String)
    fun updateLandmarkAddress(it: String)
    fun updateCategory(it: String)
    fun submit()

}