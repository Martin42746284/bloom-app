package com.example.plantdiscovery.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    background = BackgroundLight,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    error = ErrorRed
)

@Composable
fun PlantDiscoveryTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}