package com.wisdomcare.app.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Semantic roles shared by every product theme. Concrete colors belong in themes/.
 * Components consume roles (for example, warning), never an appearance such as green.
 * Foreground roles are independent so a future palette can change contrast pairings.
 */
@Immutable
data class QMDJThemeColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryPressed: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryPressed: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val background: Color,
    val surface: Color,
    val card: Color,
    val surfaceVariant: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val error: Color,
    val onError: Color,
    val errorPressed: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
    val success: Color,
    val info: Color,
    val achievement: Color,
    val onAchievement: Color,
    val achievementContainer: Color,
    val onAchievementContainer: Color,
    val outline: Color,
    val divider: Color,
    val focus: Color,
    val scrim: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val inversePrimary: Color,
)

/**
 * Bridge for existing Material components. Every Material role is explicitly mapped
 * so template colors cannot leak into QMDJ. lightColorScheme is only a factory here;
 * all its values are replaced, allowing future approved dark palettes as well.
 */
internal fun QMDJThemeColors.toMaterialColorScheme(): ColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    inversePrimary = inversePrimary,
    secondary = secondary,
    onSecondary = onSecondary,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    tertiary = achievement,
    onTertiary = onAchievement,
    tertiaryContainer = achievementContainer,
    onTertiaryContainer = onAchievementContainer,
    background = background,
    onBackground = textPrimary,
    surface = surface,
    onSurface = textPrimary,
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = textSecondary,
    surfaceTint = primary,
    inverseSurface = inverseSurface,
    inverseOnSurface = inverseOnSurface,
    error = error,
    onError = onError,
    errorContainer = errorContainer,
    onErrorContainer = onErrorContainer,
    outline = outline,
    outlineVariant = divider,
    scrim = scrim,
    surfaceBright = surface,
    surfaceDim = surfaceVariant,
    surfaceContainer = card,
    surfaceContainerHigh = surfaceVariant,
    surfaceContainerHighest = surfaceVariant,
    surfaceContainerLow = card,
    surfaceContainerLowest = surface,
    primaryFixed = primaryContainer,
    primaryFixedDim = primaryContainer,
    onPrimaryFixed = onPrimaryContainer,
    onPrimaryFixedVariant = onPrimaryContainer,
    secondaryFixed = secondaryContainer,
    secondaryFixedDim = secondaryContainer,
    onSecondaryFixed = onSecondaryContainer,
    onSecondaryFixedVariant = onSecondaryContainer,
    tertiaryFixed = achievementContainer,
    tertiaryFixedDim = achievementContainer,
    onTertiaryFixed = onAchievementContainer,
    onTertiaryFixedVariant = onAchievementContainer,
)
