package com.wisdomcare.app.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.wisdomcare.app.R
import com.wisdomcare.app.ui.components.QAction
import com.wisdomcare.app.ui.components.QButton
import com.wisdomcare.app.ui.components.QCard
import com.wisdomcare.app.ui.components.QInput
import com.wisdomcare.app.ui.theme.QMDJTheme

@Composable
fun LoginScreen(
    phone: String,
    password: String,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    modifier: Modifier = Modifier,
) = AuthForm(
    registering = false, phone = phone, password = password,
    confirmation = "", onPhoneChange = onPhoneChange, onPasswordChange = onPasswordChange,
    onConfirmationChange = {}, onSubmit = onLogin, onNavigate = onRegister, modifier = modifier,
)

@Composable
fun RegisterScreen(
    phone: String,
    password: String,
    confirmation: String,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmationChange: (String) -> Unit,
    onRegister: () -> Unit,
    onBackToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) = AuthForm(
    registering = true, phone = phone, password = password, confirmation = confirmation,
    onPhoneChange = onPhoneChange, onPasswordChange = onPasswordChange,
    onConfirmationChange = onConfirmationChange, onSubmit = onRegister,
    onNavigate = onBackToLogin, modifier = modifier,
)

@Composable
private fun AuthForm(
    registering: Boolean,
    phone: String,
    password: String,
    confirmation: String,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmationChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier,
) {
    val spacing = QMDJTheme.spacing
    val colors = QMDJTheme.colors
    val typography = QMDJTheme.typography
    val prefix = if (registering) "register" else "login"
    val focusManager = LocalFocusManager.current
    val phoneFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val confirmationFocus = remember { FocusRequester() }
    var submitted by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val phoneError = AuthValidation.phoneError(phone)
    val passwordError = AuthValidation.passwordError(password)
    val confirmationError = if (registering) AuthValidation.confirmationError(password, confirmation) else null
    val submit = {
        submitted = true
        when {
            phoneError != null -> phoneFocus.requestFocus()
            passwordError != null -> passwordFocus.requestFocus()
            confirmationError != null -> confirmationFocus.requestFocus()
            else -> {
                focusManager.clearFocus()
                onSubmit()
            }
        }
    }
    Scaffold(modifier = modifier.fillMaxSize(), containerColor = colors.background) { insets ->
        Box(Modifier.fillMaxSize().padding(insets).imePadding(), contentAlignment = Alignment.TopCenter) {
            Column(
                Modifier.widthIn(max = spacing.maxReadingWidth).fillMaxWidth()
                    .verticalScroll(rememberScrollState()).padding(spacing.screenInset),
                verticalArrangement = Arrangement.spacedBy(spacing.sectionGap),
            ) {
                if (registering) {
                    QButton(stringResource(R.string.auth_back_login), {
                        focusManager.clearFocus()
                        onNavigate()
                    }, action = QAction.Text)
                }
                Column(verticalArrangement = Arrangement.spacedBy(spacing.relatedGap)) {
                    Text(stringResource(R.string.auth_brand), style = typography.title, color = colors.primary)
                    Text(
                        stringResource(if (registering) R.string.auth_register_title else R.string.auth_login_title),
                        style = typography.headline, color = colors.textPrimary,
                        modifier = Modifier.semantics { heading() }.testTag("${prefix}_title"),
                    )
                    Text(
                        stringResource(if (registering) R.string.auth_register_subtitle else R.string.auth_login_subtitle),
                        style = typography.body, color = colors.textSecondary,
                    )
                }
                QCard(Modifier.fillMaxWidth()) {
                    QInput(
                        value = phone, onValueChange = onPhoneChange,
                        label = stringResource(R.string.auth_phone),
                        fieldModifier = Modifier.focusRequester(phoneFocus).testTag("${prefix}_phone"),
                        error = if (submitted) phoneError?.let { stringResource(it) } else null,
                        help = stringResource(R.string.auth_phone_help),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
                    )
                    QInput(
                        value = password, onValueChange = onPasswordChange,
                        label = stringResource(R.string.auth_password),
                        fieldModifier = Modifier.focusRequester(passwordFocus).testTag("${prefix}_password"),
                        error = if (submitted) passwordError?.let { stringResource(it) } else null,
                        help = stringResource(R.string.auth_password_help),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = if (registering) ImeAction.Next else ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { confirmationFocus.requestFocus() }, onDone = { submit() },
                        ),
                    )
                    if (registering) {
                        QInput(
                            value = confirmation, onValueChange = onConfirmationChange,
                            label = stringResource(R.string.auth_confirmation),
                            fieldModifier = Modifier.focusRequester(confirmationFocus).testTag("register_confirmation"),
                            error = if (submitted) confirmationError?.let { stringResource(it) } else null,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { submit() }),
                        )
                    }
                    QButton(
                        stringResource(if (passwordVisible) R.string.auth_hide_password else R.string.auth_show_password),
                        { passwordVisible = !passwordVisible },
                        modifier = Modifier.fillMaxWidth().testTag("${prefix}_password_visibility"),
                        action = QAction.Text,
                    )
                    QButton(
                        stringResource(if (registering) R.string.auth_register_action else R.string.auth_login_action),
                        { submit() }, modifier = Modifier.fillMaxWidth().testTag("${prefix}_submit"),
                    )
                }
                Text(stringResource(R.string.auth_demo_notice), style = typography.body, color = colors.textSecondary)
                QButton(
                    stringResource(if (registering) R.string.auth_existing_account else R.string.auth_create_account),
                    { focusManager.clearFocus(); onNavigate() },
                    modifier = Modifier.fillMaxWidth().testTag("${prefix}_navigate"),
                    action = QAction.Secondary,
                )
            }
        }
    }
}

@Preview(name = "Login", showBackground = true, widthDp = 360)
@Preview(name = "Login large text", showBackground = true, widthDp = 320, fontScale = 2f)
@Composable
private fun LoginPreview() {
    QMDJTheme { LoginScreen("", "", {}, {}, {}, {}) }
}

@Preview(name = "Register", showBackground = true, widthDp = 360)
@Preview(name = "Register large text", showBackground = true, widthDp = 320, fontScale = 2f)
@Composable
private fun RegisterPreview() {
    QMDJTheme { RegisterScreen("", "", "", {}, {}, {}, {}, {}) }
}
