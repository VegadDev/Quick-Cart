package com.dev.quickcart.ui.theme

import android.app.Activity
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity

private val lightColorScheme = lightColorScheme(
    primary = customAppColorsLight.primary,
    onPrimary = customAppColorsLight.onPrimary,
    secondary = customAppColorsLight.secondary,
    onSecondary = customAppColorsLight.onSecondary,
    error = customAppColorsLight.error,
    onError = customAppColorsLight.onError,
    outline = customAppColorsLight.outline,
    background = customAppColorsLight.background,
    onBackground = customAppColorsLight.onBackground,
)

private val darkColorScheme = darkColorScheme(
    primary = customAppColorsDark.primary,
    onPrimary = customAppColorsDark.onPrimary,
    secondary = customAppColorsDark.secondary,
    onSecondary = customAppColorsDark.onSecondary,
    error = customAppColorsDark.error,
    onError = customAppColorsDark.onError,
    outline = customAppColorsDark.outline,
    background = customAppColorsDark.background,
    onBackground = customAppColorsDark.onBackground,
)


val LocalAppColor = compositionLocalOf { customAppColorsLight }


object AppTheme {

    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColor.current

    val activity: Activity?
        @Composable
        @ReadOnlyComposable
        get() {
            var context = LocalContext.current
            while (context is ContextWrapper) {
                if (context is Activity) {
                    return context
                }
                context = context.baseContext
            }
            return null
        }
}



@Composable
fun QuickCartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val density = LocalDensity.current

    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val quickCartColors = if (darkTheme) customAppColorsDark else customAppColorsLight

    MaterialTheme(
        colorScheme = colorScheme,
        content = {
            CompositionLocalProvider(
                LocalAppColor provides quickCartColors,
            ) {
                content()
            }
        }
    )
}