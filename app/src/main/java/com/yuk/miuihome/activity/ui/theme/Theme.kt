package com.yuk.miuihome.activity.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = Teal200,
    onPrimary = Black,
    onSecondary = Black,
    background = Black,
    onBackground = White
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = Teal700,
    onPrimary = White,
    onSecondary = Black,
    background = White,
    onBackground = Black
)

@Composable
fun OwnTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        content = content
    )
}