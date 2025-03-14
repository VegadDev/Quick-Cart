package com.dev.quickcart.screens.addProduct

interface AddProductInterActor {


    fun updateProdName(it: String)
    fun updateProdImage(it: List<String>)
    fun updateProdPrice(it: String)
    fun updateProdDescription(it: String)
    //fun updateProdCategory(it: String)
    //fun updateProdType(it: String)
    fun updateProdTypeValue(it: String)
    fun updateProdProtein(it: String)
    fun updateProdCalories(it: String)
    fun updateProdFat(it: String)
    fun updateProdFiber(it: String)


    fun submit()




}