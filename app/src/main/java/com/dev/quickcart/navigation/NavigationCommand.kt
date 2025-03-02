package com.dev.quickcart.navigation

sealed class NavigationCommand {

    abstract class RouteCommand(val route: String, val json: String?) : NavigationCommand() {
        val routeWithArgs: String get() = json?.let { "${route}?json=$it" } ?: route
    }

    class To(route: String, json: String? = null) : RouteCommand(route, json)
    class ToAndClear(route: String, json: String? = null) : RouteCommand(route, json)
    class ToAndClearAll(route: String, json: String? = null) : RouteCommand(route, json)

    data object Back : NavigationCommand()
    data object Idle : NavigationCommand()

}