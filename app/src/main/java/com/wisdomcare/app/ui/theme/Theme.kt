package com.wisdomcare.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.wisdomcare.app.ui.theme.themes.SerenityTheme

// Fail early rather than silently binding a component to one particular product style.
private val LocalQMDJTheme = staticCompositionLocalOf<QMDJThemeDefinition> {
    error("QMDJ tokens require a QMDJTheme provider around the content.")
}

/**
 * Semantic accessors for screens/components. The nearest provider wins, including
 * nested previews. A theme change replaces every group together on recomposition.
 */
object QMDJTheme {
    val colors: QMDJThemeColors
        @Composable @ReadOnlyComposable get() = LocalQMDJTheme.current.colors

    val typography: QMDJTypography
        @Composable @ReadOnlyComposable get() = LocalQMDJTheme.current.typography

    val shapes: QMDJShapes
        @Composable @ReadOnlyComposable get() = LocalQMDJTheme.current.shapes

    val spacing: QMDJSpacing
        @Composable @ReadOnlyComposable get() = LocalQMDJTheme.current.spacing

    val gradients: QMDJGradients
        @Composable @ReadOnlyComposable get() = LocalQMDJTheme.current.gradients
}

/**
 * Theme engine: no palette, radius, font size, or brush definitions live here.
 * Serenity is only the default argument, not the only supported visual style.
 * System dark mode/wallpaper colors never replace an explicitly selected definition;
 * callers may select an approved dark definition at the app root when one exists.
 */
@Composable
fun QMDJTheme(
    theme: QMDJThemeDefinition = SerenityTheme,
    content: @Composable () -> Unit,
) {
    val materialColors = remember(theme.colors) { theme.colors.toMaterialColorScheme() }
    val materialTypography = remember(theme.typography) { theme.typography.toMaterialTypography() }
    val materialShapes = remember(theme.shapes) { theme.shapes.toMaterialShapes() }

    CompositionLocalProvider(LocalQMDJTheme provides theme) {
        MaterialTheme(
            colorScheme = materialColors,
            typography = materialTypography,
            shapes = materialShapes,
            content = content,
        )
    }
}

/** Keeps existing app/preview call sites working without touching screen or business code. */
@Deprecated(
    message = "Use QMDJTheme for new code.",
    replaceWith = ReplaceWith("QMDJTheme(theme = theme, content = content)"),
)
@Composable
fun WisdomcareTheme(
    theme: QMDJThemeDefinition = SerenityTheme,
    content: @Composable () -> Unit,
) {
    QMDJTheme(theme = theme, content = content)
}
