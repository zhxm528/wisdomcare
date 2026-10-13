package com.wisdomcare.app.ui.theme

import androidx.compose.runtime.Immutable

/**
 * Complete, immutable theme data, independent of the Compose provider engine.
 * Add a definition in themes/ with all five token groups, then pass it to QMDJTheme.
 * Avoid enums or theme-name branches in components: any definition uses this contract.
 */
@Immutable
data class QMDJThemeDefinition(
    val colors: QMDJThemeColors,
    val typography: QMDJTypography,
    val shapes: QMDJShapes,
    val spacing: QMDJSpacing,
    val gradients: QMDJGradients,
)
