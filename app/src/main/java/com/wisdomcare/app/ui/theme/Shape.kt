package com.wisdomcare.app.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable

/** Shape roles allow future themes to replace geometry without editing components. */
@Immutable
data class QMDJShapes(
    val small: CornerBasedShape,
    val medium: CornerBasedShape,
    val large: CornerBasedShape,
    val card: CornerBasedShape,
    val button: CornerBasedShape,
    val input: CornerBasedShape,
    val dialog: CornerBasedShape,
)

internal fun QMDJShapes.toMaterialShapes(): Shapes = Shapes(
    extraSmall = small,
    small = small,
    medium = medium,
    large = large,
    extraLarge = dialog,
)
