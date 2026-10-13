package com.wisdomcare.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.VisualTransformation
import com.wisdomcare.app.ui.theme.QMDJTheme

/** Persistent label and full-size help/error text; native editing and focus remain intact. */
@Composable
fun QInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    help: String? = null,
    error: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val colors = QMDJTheme.colors
    Column(modifier, verticalArrangement = Arrangement.spacedBy(QMDJTheme.spacing.relatedGap)) {
        // An external label never shrinks to Material's floating-label size at large font scales.
        Text(label, style = QMDJTheme.typography.label, color = colors.textPrimary)
        OutlinedTextField(
            value = value, onValueChange = onValueChange,
            modifier = fieldModifier.fillMaxWidth().heightIn(min = QMDJTheme.spacing.inputHeight)
                .semantics {
                    // Native editable text retains its editing actions and password semantics.
                    contentDescription = label
                    if (error != null) error(error)
                },
            enabled = enabled, singleLine = true, isError = error != null,
            shape = QMDJTheme.shapes.input,
            textStyle = QMDJTheme.typography.body,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions, keyboardActions = keyboardActions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.surface,
                unfocusedContainerColor = colors.surface,
                errorContainerColor = colors.surface,
                disabledContainerColor = colors.surfaceVariant,
                focusedTextColor = colors.textPrimary,
                unfocusedTextColor = colors.textPrimary,
                disabledTextColor = colors.textSecondary,
                errorTextColor = colors.textPrimary,
                cursorColor = colors.focus,
                errorCursorColor = colors.error,
                focusedBorderColor = colors.focus,
                unfocusedBorderColor = colors.outline,
                disabledBorderColor = colors.outline,
                errorBorderColor = colors.error,
            ),
        )
        (error ?: help)?.let {
            Text(it, style = QMDJTheme.typography.body,
                color = if (error != null) colors.error else colors.textSecondary)
        }
    }
}
