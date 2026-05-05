package com.sunnyweather.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = SunGlow,
    onPrimary = Color(0xFF1A1A1A),
    secondary = SkyBottom,
    onSecondary = TextOnSky,
    background = SkyTop,
    onBackground = TextOnSky,
    surface = CardGlass,
    onSurface = TextOnSky,
)

@Composable
fun SunnyWeatherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content,
    )
}
