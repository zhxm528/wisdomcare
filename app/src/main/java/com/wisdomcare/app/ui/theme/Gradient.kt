package com.wisdomcare.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush

/**
 * Theme-owned brushes. Screens select a purpose, never construct a gradient.
 * A future minimal theme may supply SolidColor brushes using the same contract.
 */
@Immutable
data class QMDJGradients(
    val backgroundGradient: Brush,
    val healthCardGradient: Brush,
    val highlightGradient: Brush,
    val informationCardGradient: Brush,
)
