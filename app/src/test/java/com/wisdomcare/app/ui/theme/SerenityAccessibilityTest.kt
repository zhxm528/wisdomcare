package com.wisdomcare.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.wisdomcare.app.ui.theme.themes.SerenityTheme
import org.junit.Assert.assertTrue
import org.junit.Test

class SerenityAccessibilityTest {
    @Test
    fun informativeForegroundsMeetContrastRequirements() {
        val colors = SerenityTheme.colors
        val textPairs = buildList {
            listOf(colors.background, colors.surface, colors.card, colors.surfaceVariant).forEach {
                add(colors.textPrimary to it)
                add(colors.textSecondary to it)
            }
            add(colors.onPrimary to colors.primary)
            add(colors.onPrimary to colors.primaryPressed)
            add(colors.onPrimaryContainer to colors.primaryContainer)
            add(colors.onSecondary to colors.secondary)
            add(colors.onSecondary to colors.secondaryPressed)
            add(colors.onSecondaryContainer to colors.secondaryContainer)
            add(colors.onError to colors.error)
            add(colors.onError to colors.errorPressed)
            add(colors.onErrorContainer to colors.errorContainer)
            add(colors.onWarning to colors.warning)
            add(colors.onWarningContainer to colors.warningContainer)
            add(colors.onAchievement to colors.achievement)
            add(colors.onAchievementContainer to colors.achievementContainer)
            add(colors.inverseOnSurface to colors.inverseSurface)
            add(colors.inversePrimary to colors.inverseSurface)
            // Gradient stops are also the darkest approved light container surfaces.
            add(colors.textPrimary to colors.warningContainer)
            add(colors.textPrimary to colors.primaryContainer)
            add(colors.textPrimary to colors.secondaryContainer)
        }
        textPairs.forEach { (foreground, background) ->
            assertContrast(foreground, background, 4.5f)
        }
    }

    @Test
    fun essentialBoundariesAndFocusRemainVisible() {
        val colors = SerenityTheme.colors
        listOf(colors.background, colors.surface, colors.card).forEach { background ->
            assertContrast(colors.outline, background, 3f)
            assertContrast(colors.focus, background, 3f)
        }
    }

    private fun assertContrast(foreground: Color, background: Color, minimum: Float) {
        val first = foreground.luminance()
        val second = background.luminance()
        val ratio = (maxOf(first, second) + 0.05f) / (minOf(first, second) + 0.05f)
        assertTrue("Contrast $ratio is below $minimum for $foreground on $background", ratio >= minimum)
    }
}
