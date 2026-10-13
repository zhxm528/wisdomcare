package com.wisdomcare.app.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wisdomcare.app.ui.theme.themes.SerenityTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QMDJThemeTest {
    @get:Rule
    val compose = createComposeRule()

    // A distinct test fixture, not a proposed production style or new screen.
    private val alternate = SerenityTheme.copy(
        colors = SerenityTheme.colors.copy(
            primary = SerenityTheme.colors.secondary,
            surface = SerenityTheme.colors.primaryContainer,
        ),
        typography = SerenityTheme.typography.copy(body = SerenityTheme.typography.title),
        shapes = SerenityTheme.shapes.copy(medium = SerenityTheme.shapes.small),
        spacing = SerenityTheme.spacing.copy(screenInset = SerenityTheme.spacing.cardGap),
        gradients = SerenityTheme.gradients.copy(
            backgroundGradient = SolidColor(SerenityTheme.colors.background),
        ),
    )

    @Test
    fun changingRootDefinitionUpdatesAllTokensAndMaterialWithoutChangingContent() {
        val selected = mutableStateOf(SerenityTheme)
        var observed: Snapshot? = null
        compose.setContent {
            QMDJTheme(theme = selected.value) {
                Capture { observed = it }
            }
        }
        compose.runOnIdle {
            assertSnapshot(SerenityTheme, observed!!)
            selected.value = alternate
        }
        compose.runOnIdle {
            assertSnapshot(alternate, observed!!)
        }
    }

    @Test
    fun nestedDefinitionIsScopedAndLegacyEntryPointUsesTheSameProvider() {
        var before: Snapshot? = null
        var inside: Snapshot? = null
        var after: Snapshot? = null
        compose.setContent {
            LegacyTheme {
                Capture { before = it }
                QMDJTheme(theme = alternate) {
                    Capture { inside = it }
                }
                Capture { after = it }
            }
        }
        compose.runOnIdle {
            assertSnapshot(SerenityTheme, before!!)
            assertSnapshot(alternate, inside!!)
            assertSnapshot(SerenityTheme, after!!)
        }
    }

    @Suppress("DEPRECATION")
    @Composable
    private fun LegacyTheme(content: @Composable () -> Unit) {
        WisdomcareTheme(content = content)
    }

    @Composable
    private fun Capture(onSnapshot: (Snapshot) -> Unit) {
        val snapshot = Snapshot(
            QMDJTheme.colors,
            QMDJTheme.typography,
            QMDJTheme.shapes,
            QMDJTheme.spacing,
            QMDJTheme.gradients,
            MaterialTheme.colorScheme,
            MaterialTheme.typography,
            MaterialTheme.shapes,
        )
        SideEffect { onSnapshot(snapshot) }
    }

    private fun assertSnapshot(expected: QMDJThemeDefinition, actual: Snapshot) {
        assertSame(expected.colors, actual.colors)
        assertSame(expected.typography, actual.typography)
        assertSame(expected.shapes, actual.shapes)
        assertSame(expected.spacing, actual.spacing)
        assertSame(expected.gradients, actual.gradients)
        assertEquals(expected.colors.primary, actual.materialColors.primary)
        assertEquals(expected.colors.surface, actual.materialColors.surface)
        assertEquals(expected.typography.body, actual.materialTypography.bodyLarge)
        assertEquals(expected.shapes.medium, actual.materialShapes.medium)
    }

    private data class Snapshot(
        val colors: QMDJThemeColors,
        val typography: QMDJTypography,
        val shapes: QMDJShapes,
        val spacing: QMDJSpacing,
        val gradients: QMDJGradients,
        val materialColors: ColorScheme,
        val materialTypography: Typography,
        val materialShapes: Shapes,
    )
}
