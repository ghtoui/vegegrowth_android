package com.moritoui.vegegrowthapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.moritoui.vegegrowthapp.ui.color.backgroundDark
import com.moritoui.vegegrowthapp.ui.color.backgroundLight
import com.moritoui.vegegrowthapp.ui.color.errorContainerDark
import com.moritoui.vegegrowthapp.ui.color.errorContainerLight
import com.moritoui.vegegrowthapp.ui.color.errorDark
import com.moritoui.vegegrowthapp.ui.color.errorLight
import com.moritoui.vegegrowthapp.ui.color.inverseOnSurfaceDark
import com.moritoui.vegegrowthapp.ui.color.inverseOnSurfaceLight
import com.moritoui.vegegrowthapp.ui.color.inversePrimaryDark
import com.moritoui.vegegrowthapp.ui.color.inversePrimaryLight
import com.moritoui.vegegrowthapp.ui.color.inverseSurfaceDark
import com.moritoui.vegegrowthapp.ui.color.inverseSurfaceLight
import com.moritoui.vegegrowthapp.ui.color.onBackgroundDark
import com.moritoui.vegegrowthapp.ui.color.onBackgroundLight
import com.moritoui.vegegrowthapp.ui.color.onErrorContainerDark
import com.moritoui.vegegrowthapp.ui.color.onErrorContainerLight
import com.moritoui.vegegrowthapp.ui.color.onErrorDark
import com.moritoui.vegegrowthapp.ui.color.onErrorLight
import com.moritoui.vegegrowthapp.ui.color.onPrimaryContainerDark
import com.moritoui.vegegrowthapp.ui.color.onPrimaryContainerLight
import com.moritoui.vegegrowthapp.ui.color.onPrimaryDark
import com.moritoui.vegegrowthapp.ui.color.onPrimaryLight
import com.moritoui.vegegrowthapp.ui.color.onSecondaryContainerDark
import com.moritoui.vegegrowthapp.ui.color.onSecondaryContainerLight
import com.moritoui.vegegrowthapp.ui.color.onSecondaryDark
import com.moritoui.vegegrowthapp.ui.color.onSecondaryLight
import com.moritoui.vegegrowthapp.ui.color.onSurfaceDark
import com.moritoui.vegegrowthapp.ui.color.onSurfaceLight
import com.moritoui.vegegrowthapp.ui.color.onSurfaceVariantDark
import com.moritoui.vegegrowthapp.ui.color.onSurfaceVariantLight
import com.moritoui.vegegrowthapp.ui.color.onTertiaryContainerDark
import com.moritoui.vegegrowthapp.ui.color.onTertiaryContainerLight
import com.moritoui.vegegrowthapp.ui.color.onTertiaryDark
import com.moritoui.vegegrowthapp.ui.color.onTertiaryLight
import com.moritoui.vegegrowthapp.ui.color.outlineDark
import com.moritoui.vegegrowthapp.ui.color.outlineLight
import com.moritoui.vegegrowthapp.ui.color.outlineVariantDark
import com.moritoui.vegegrowthapp.ui.color.outlineVariantLight
import com.moritoui.vegegrowthapp.ui.color.primaryContainerDark
import com.moritoui.vegegrowthapp.ui.color.primaryContainerLight
import com.moritoui.vegegrowthapp.ui.color.primaryDark
import com.moritoui.vegegrowthapp.ui.color.primaryLight
import com.moritoui.vegegrowthapp.ui.color.scrimDark
import com.moritoui.vegegrowthapp.ui.color.scrimLight
import com.moritoui.vegegrowthapp.ui.color.secondaryContainerDark
import com.moritoui.vegegrowthapp.ui.color.secondaryContainerLight
import com.moritoui.vegegrowthapp.ui.color.secondaryDark
import com.moritoui.vegegrowthapp.ui.color.secondaryLight
import com.moritoui.vegegrowthapp.ui.color.surfaceBrightDark
import com.moritoui.vegegrowthapp.ui.color.surfaceBrightLight
import com.moritoui.vegegrowthapp.ui.color.surfaceContainerDark
import com.moritoui.vegegrowthapp.ui.color.surfaceContainerHighDark
import com.moritoui.vegegrowthapp.ui.color.surfaceContainerHighLight
import com.moritoui.vegegrowthapp.ui.color.surfaceContainerHighestDark
import com.moritoui.vegegrowthapp.ui.color.surfaceContainerHighestLight
import com.moritoui.vegegrowthapp.ui.color.surfaceContainerLight
import com.moritoui.vegegrowthapp.ui.color.surfaceContainerLowDark
import com.moritoui.vegegrowthapp.ui.color.surfaceContainerLowLight
import com.moritoui.vegegrowthapp.ui.color.surfaceContainerLowestDark
import com.moritoui.vegegrowthapp.ui.color.surfaceContainerLowestLight
import com.moritoui.vegegrowthapp.ui.color.surfaceDark
import com.moritoui.vegegrowthapp.ui.color.surfaceDimDark
import com.moritoui.vegegrowthapp.ui.color.surfaceDimLight
import com.moritoui.vegegrowthapp.ui.color.surfaceLight
import com.moritoui.vegegrowthapp.ui.color.surfaceVariantDark
import com.moritoui.vegegrowthapp.ui.color.surfaceVariantLight
import com.moritoui.vegegrowthapp.ui.color.tertiaryContainerDark
import com.moritoui.vegegrowthapp.ui.color.tertiaryContainerLight
import com.moritoui.vegegrowthapp.ui.color.tertiaryDark
import com.moritoui.vegegrowthapp.ui.color.tertiaryLight

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark
)

@Composable
fun VegegrowthAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> darkScheme
            else -> lightScheme
        }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
