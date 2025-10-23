package com.izzy2lost.weeu.common.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Wee U brand color
private val WeeUCyan = Color(0xFF009ac7)
private val WeeUCyanLight = Color(0xFF33b0d5)
private val WeeUCyanDark = Color(0xFF007a9f)

// Dark theme with cyan background and accents
private val DarkColorScheme = darkColorScheme(
    primary = WeeUCyan,
    onPrimary = Color.White,
    primaryContainer = WeeUCyanDark,
    onPrimaryContainer = Color.White,
    secondary = WeeUCyanLight,
    onSecondary = Color.White,
    background = WeeUCyan,
    onBackground = Color.White,
    surface = WeeUCyan,
    onSurface = Color.White,
    surfaceVariant = WeeUCyanDark,
    onSurfaceVariant = Color.White,
)

// Light theme with cyan text and accents
private val LightColorScheme = lightColorScheme(
    primary = WeeUCyan,
    onPrimary = Color.White,
    primaryContainer = WeeUCyanLight,
    onPrimaryContainer = WeeUCyan,
    secondary = WeeUCyanDark,
    onSecondary = Color.White,
    background = Color.White,
    onBackground = WeeUCyan,
    surface = Color.White,
    onSurface = WeeUCyan,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = WeeUCyan,
)

/**
 * Material 3 Expressive theme for Wee U
 * Features custom cyan color scheme and expressive typography/shapes
 */
@Composable
fun WeeUTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled by default to use custom cyan colors
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ExpressiveTypography,
        shapes = ExpressiveShapes,
        content = content
    )
}


// Material 3 Expressive Typography
private val ExpressiveTypography = androidx.compose.material3.Typography(
    displayLarge = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelLarge = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = androidx.compose.ui.text.TextStyle(
        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)

// Material 3 Expressive Shapes with more rounded, playful corners
// Expressive design uses larger corner radii for a more friendly, modern feel
private val ExpressiveShapes = androidx.compose.material3.Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    small = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(28.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(36.dp),
)
