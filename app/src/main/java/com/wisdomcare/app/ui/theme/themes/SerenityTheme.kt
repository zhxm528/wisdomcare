package com.wisdomcare.app.ui.theme.themes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wisdomcare.app.ui.theme.QMDJGradients
import com.wisdomcare.app.ui.theme.QMDJShapes
import com.wisdomcare.app.ui.theme.QMDJSpacing
import com.wisdomcare.app.ui.theme.QMDJThemeColors
import com.wisdomcare.app.ui.theme.QMDJThemeDefinition
import com.wisdomcare.app.ui.theme.QMDJTypography

/** Modern Chinese Serenity (现代中式静雅), as specified in docs/UI_DESIGN_SYSTEM.md. */
private val serenityColors = QMDJThemeColors(
    primary = Color(0xFF2F6654), // 竹青 / Bamboo Green
    onPrimary = Color(0xFFFCFAF4),
    primaryPressed = Color(0xFF24503F),
    primaryContainer = Color(0xFFE2EDE5),
    onPrimaryContainer = Color(0xFF24352F),
    secondary = Color(0xFF365F7A), // 云蓝 / Cloud Blue
    onSecondary = Color(0xFFFCFAF4),
    secondaryPressed = Color(0xFF294A60),
    secondaryContainer = Color(0xFFE3EDF2),
    onSecondaryContainer = Color(0xFF24352F),
    background = Color(0xFFF6F3EB), // 宣纸白 / Rice Paper
    surface = Color(0xFFFCFAF4),
    card = Color(0xFFFCFAF4),
    surfaceVariant = Color(0xFFEAE6DC),
    textPrimary = Color(0xFF24352F),
    textSecondary = Color(0xFF526159),
    error = Color(0xFFA13F37), // 朱砂 / Muted Cinnabar
    onError = Color(0xFFFCFAF4),
    errorPressed = Color(0xFF82322D),
    errorContainer = Color(0xFFF5E4DF),
    onErrorContainer = Color(0xFFA13F37),
    warning = Color(0xFF96501F), // 暮橙 / Sunset Orange
    onWarning = Color(0xFFFCFAF4),
    warningContainer = Color(0xFFF4E5D5),
    onWarningContainer = Color(0xFF96501F),
    success = Color(0xFF2F6654),
    info = Color(0xFF365F7A),
    achievement = Color(0xFF7C6122), // 秋金 / Autumn Gold
    onAchievement = Color(0xFFFCFAF4),
    achievementContainer = Color(0xFFF1EAD4),
    onAchievementContainer = Color(0xFF7C6122),
    outline = Color(0xFF78867D),
    divider = Color(0xFFD6DCD2),
    focus = Color(0xFF365F7A),
    // Material applies modal scrim opacity. QDialog will apply the specified 32%.
    scrim = Color(0xFF24352F),
    inverseSurface = Color(0xFF24352F),
    inverseOnSurface = Color(0xFFFCFAF4),
    inversePrimary = Color(0xFFE2EDE5),
)

private fun serenityText(size: Int, lineHeight: Int, weight: FontWeight) = TextStyle(
    fontFamily = FontFamily.SansSerif, // System Chinese fallback; no downloaded font required.
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = lineHeight.sp,
    letterSpacing = 0.sp,
)

private val serenityTypography = QMDJTypography(
    display = serenityText(36, 46, FontWeight.SemiBold), // Prominent metric
    headline = serenityText(28, 40, FontWeight.SemiBold), // Screen title
    title = serenityText(22, 32, FontWeight.SemiBold), // Card title
    body = serenityText(20, 30, FontWeight.Normal),
    label = serenityText(20, 30, FontWeight.Medium),
    caption = serenityText(18, 28, FontWeight.Normal), // Minimum readable size
    sectionTitle = serenityText(24, 34, FontWeight.SemiBold),
)

private val serenityShapes = QMDJShapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(20.dp),
    card = RoundedCornerShape(20.dp),
    button = RoundedCornerShape(12.dp),
    input = RoundedCornerShape(12.dp),
    dialog = RoundedCornerShape(24.dp),
)

private val serenitySpacing = QMDJSpacing(
    detailGap = 4.dp,
    relatedGap = 8.dp,
    controlGap = 12.dp,
    screenInset = 20.dp,
    compactScreenInset = 16.dp,
    cardPadding = 20.dp,
    compactCardPadding = 16.dp,
    cardGap = 16.dp,
    sectionGap = 24.dp,
    dialogPadding = 24.dp,
    touchTarget = 56.dp,
    primaryActionHeight = 60.dp,
    inputHeight = 60.dp,
    iconSize = 24.dp,
    navigationIconSize = 28.dp,
    outlineWidth = 1.dp,
    focusWidth = 2.dp,
    cardElevation = 0.dp,
    dialogElevation = 6.dp,
    maxReadingWidth = 600.dp,
)

private val serenityGradients = QMDJGradients(
    backgroundGradient = Brush.verticalGradient(
        colors = listOf(serenityColors.background, serenityColors.primaryContainer),
    ), // 山岚 / Mountain mist
    healthCardGradient = Brush.verticalGradient(
        colors = listOf(serenityColors.primaryContainer, serenityColors.surface),
    ), // 竹影 / Bamboo shade
    highlightGradient = Brush.verticalGradient(
        colors = listOf(serenityColors.warningContainer, serenityColors.surface),
    ), // 晨霞 / Soft sunrise, decorative rather than a severity indicator
    informationCardGradient = Brush.verticalGradient(
        colors = listOf(serenityColors.secondaryContainer, serenityColors.surface),
    ), // 晨空 / Morning sky
)

/**
 * This is data, not a provider. Future Technology/Healthcare themes implement the
 * same definition; components keep consuming QMDJTheme's semantic accessors.
 * Only the approved light palette exists; no automatic dark or dynamic variants.
 */
val SerenityTheme = QMDJThemeDefinition(
    colors = serenityColors,
    typography = serenityTypography,
    shapes = serenityShapes,
    spacing = serenitySpacing,
    gradients = serenityGradients,
)
