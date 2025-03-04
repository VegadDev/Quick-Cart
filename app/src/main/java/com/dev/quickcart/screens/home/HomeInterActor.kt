package com.dev.quickcart.screens.home

interface HomeInterActor {

    fun gotoProfile()
    fun updateSearchInput(it: String)

}


object DefaultHomeInterActor: HomeInterActor{
    override fun gotoProfile() {}
    override fun updateSearchInput(it: String) {}
}