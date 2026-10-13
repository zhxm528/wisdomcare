package com.wisdomcare.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/** Layout dimensions are semantic too: screens must not supply their own dp values. */
@Immutable
data class QMDJSpacing(
    val detailGap: Dp,
    val relatedGap: Dp,
    val controlGap: Dp,
    val screenInset: Dp,
    val compactScreenInset: Dp,
    val cardPadding: Dp,
    val compactCardPadding: Dp,
    val cardGap: Dp,
    val sectionGap: Dp,
    val dialogPadding: Dp,
    val touchTarget: Dp,
    val primaryActionHeight: Dp,
    val inputHeight: Dp,
    val iconSize: Dp,
    val navigationIconSize: Dp,
    val outlineWidth: Dp,
    val focusWidth: Dp,
    val cardElevation: Dp,
    val dialogElevation: Dp,
    val maxReadingWidth: Dp,
)
