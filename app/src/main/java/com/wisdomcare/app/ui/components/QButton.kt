package com.wisdomcare.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.wisdomcare.app.ui.theme.QMDJTheme

enum class QAction { Primary, Secondary, Text }

/** Native button semantics and interaction, with centrally applied QMDJ tokens. */
@Composable
fun QButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    action: QAction = QAction.Primary,
    enabled: Boolean = true,
) {
    val colors = QMDJTheme.colors
    val spacing = QMDJTheme.spacing
    val shape = QMDJTheme.shapes.button
    val interactions = remember { MutableInteractionSource() }
    val pressed by interactions.collectIsPressedAsState()
    val focused by interactions.collectIsFocusedAsState()
    val target = modifier
        .widthIn(min = spacing.touchTarget)
        .heightIn(min = if (action == QAction.Primary) spacing.primaryActionHeight else spacing.touchTarget)
        .then(if (focused) Modifier.border(spacing.focusWidth, colors.focus, shape)
            .padding(spacing.focusWidth) else Modifier)
    val label: @Composable () -> Unit = { Text(text, style = QMDJTheme.typography.label) }
    val contentPadding = PaddingValues(horizontal = spacing.cardPadding, vertical = spacing.controlGap)
    when (action) {
        QAction.Primary -> Button(
            onClick = onClick,
            modifier = target,
            enabled = enabled,
            shape = shape,
            interactionSource = interactions,
            contentPadding = contentPadding,
            border = if (focused) BorderStroke(spacing.focusWidth, colors.onPrimary) else null,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (pressed) colors.primaryPressed else colors.primary,
                contentColor = colors.onPrimary,
                disabledContainerColor = colors.surfaceVariant,
                disabledContentColor = colors.textSecondary,
            ),
        ) { label() }
        QAction.Secondary -> OutlinedButton(
            onClick = onClick, modifier = target, enabled = enabled,
            shape = shape, interactionSource = interactions,
            contentPadding = contentPadding,
            border = BorderStroke(spacing.outlineWidth, colors.outline),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.primary,
                disabledContentColor = colors.textSecondary,
            ),
        ) { label() }
        QAction.Text -> TextButton(
            onClick = onClick, modifier = target, enabled = enabled,
            shape = shape, interactionSource = interactions,
            contentPadding = contentPadding,
            colors = ButtonDefaults.textButtonColors(
                contentColor = colors.secondary,
                disabledContentColor = colors.textSecondary,
            ),
        ) { label() }
    }
}
