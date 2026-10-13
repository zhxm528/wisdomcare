package com.wisdomcare.app.ui.components

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import com.wisdomcare.app.ui.theme.QMDJTheme

@Composable
fun QDialog(
    title: String,
    message: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        shape = QMDJTheme.shapes.dialog,
        containerColor = QMDJTheme.colors.surface,
        titleContentColor = QMDJTheme.colors.textPrimary,
        textContentColor = QMDJTheme.colors.textPrimary,
        tonalElevation = QMDJTheme.spacing.dialogElevation,
        title = { Text(title, style = QMDJTheme.typography.sectionTitle) },
        text = {
            val view = LocalView.current
            SideEffect {
                // Design-system modal opacity; Android's native dialog owns dimming and dismissal.
                (view.parent as? DialogWindowProvider)?.window?.setDimAmount(0.32f)
            }
            Text(message, style = QMDJTheme.typography.body,
                modifier = Modifier.verticalScroll(rememberScrollState()))
        },
        confirmButton = { QButton(confirmLabel, onConfirm) },
    )
}
