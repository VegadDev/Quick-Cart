package com.dev.quickcart.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class Navigator
@Inject
constructor() : ViewModel() {

    private val _navigation = Channel<NavigationEvent>(Channel.BUFFERED)
    val navigation = _navigation.receiveAsFlow()

    fun navigate(command: NavigationCommand) {
        _navigation.trySend(NavigationEvent(command = command))
    }

}

data class NavigationEvent(

    val command: NavigationCommand,
    val id: Long = System.currentTimeMillis()

)


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

data class Screen(
    val route : String,
    val content: @Composable (NavBackStackEntry) -> Unit
)
