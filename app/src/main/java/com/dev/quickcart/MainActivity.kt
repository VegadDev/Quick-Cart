package com.dev.quickcart

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.NavigationEvent
import com.dev.quickcart.navigation.Navigator
import com.dev.quickcart.navigation.handleNavigation
import com.dev.quickcart.navigation.screens
import com.dev.quickcart.ui.theme.AppTheme
import com.dev.quickcart.ui.theme.QuickCartTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuickCartTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                        //.animateContentSize(),
                    containerColor = AppTheme.colors.background,
                ) {
                    QuickCartNav()
                }
            }
        }
    }
}

@Composable
fun QuickCartNav(
    navigator: Navigator = hiltViewModel()
) {
    val navController = rememberNavController()
    val navigationCommands by navigator.navigation.collectAsState(
        initial = NavigationEvent(NavigationCommand.Idle)
    )

    LaunchedEffect(navigationCommands) {
        navController.handleNavigation(navigationCommands.command)
    }

    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.route,
        enterTransition = { fadeIn(animationSpec = tween(500)) },
        exitTransition = { fadeOut(animationSpec = tween(100)) },
        popEnterTransition = { fadeIn(animationSpec = tween(500)) },
        popExitTransition = { fadeOut(animationSpec = tween(100)) }
    ) {
        screens.forEach { screen ->
            composable(
                route = "${screen.route}?json={json}",
                arguments = listOf(navArgument("json") {
                    type = NavType.StringType
                    defaultValue = ""
                }),
            ) { backStackEntry ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(durationMillis = 500)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 100))
                ) {
                    screen.content(backStackEntry)
                }

            }
        }
    }
}

