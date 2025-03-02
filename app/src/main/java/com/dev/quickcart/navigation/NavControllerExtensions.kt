package com.dev.quickcart.navigation

import android.util.Log
import androidx.navigation.NavController

fun NavController.handleNavigation(command: NavigationCommand) {
    when (command) {
        NavigationCommand.Back -> {
            if (previousBackStackEntry != null) navigateUp()
            else Log.d("Nav", "No back stack entry available")
        }
        is NavigationCommand.RouteCommand -> {
            when (command) {
                is NavigationCommand.To -> navigate(command.routeWithArgs)
                is NavigationCommand.ToAndClear -> {
                    navigate(command.routeWithArgs) {
                        popUpTo(graph.startDestinationId) { inclusive = false }
                    }
                }
                is NavigationCommand.ToAndClearAll -> {
                    navigate(command.routeWithArgs) {
                        popUpTo(graph.id) { inclusive = true }
                    }
                }

                else -> {
                    Log.e("Nav", "Unhandled Navigation Command: $command")
                }
            }
        }
        NavigationCommand.Idle -> Unit
    }
}
