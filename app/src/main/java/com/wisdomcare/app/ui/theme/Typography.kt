package com.wisdomcare.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

/** Use QMDJTheme.typography roles in UI code, not local TextStyle or font sizes. */
@Immutable
data class QMDJTypography(
    val display: TextStyle,
    val headline: TextStyle,
    val title: TextStyle,
    val body: TextStyle,
    val label: TextStyle,
    val caption: TextStyle,
    val sectionTitle: TextStyle,
)

/** Fill all slots: even Material's small labels must retain elderly-friendly sizes. */
internal fun QMDJTypography.toMaterialTypography(): Typography = Typography(
    displayLarge = display,
    displayMedium = display,
    displaySmall = display,
    headlineLarge = headline,
    headlineMedium = sectionTitle,
    headlineSmall = title,
    titleLarge = sectionTitle,
    titleMedium = title,
    titleSmall = title,
    bodyLarge = body,
    bodyMedium = body,
    bodySmall = caption,
    labelLarge = label,
    labelMedium = label,
    labelSmall = caption,
)
