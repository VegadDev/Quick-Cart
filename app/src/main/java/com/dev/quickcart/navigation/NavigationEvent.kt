package com.dev.quickcart.navigation

data class NavigationEvent(

    val command: NavigationCommand,
    val id: Long = System.currentTimeMillis()

)