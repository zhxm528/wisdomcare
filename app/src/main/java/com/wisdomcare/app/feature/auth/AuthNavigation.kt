package com.wisdomcare.app.feature.auth

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wisdomcare.app.R
import com.wisdomcare.app.ui.components.QDialog

private enum class AuthDestination { Login, Register }

/** A bounded two-page stack: Back always returns from registration to login. */
@Composable
fun AuthNavigation(modifier: Modifier = Modifier) {
    var destination by rememberSaveable { mutableStateOf(AuthDestination.Login) }
    var loginPhone by rememberSaveable { mutableStateOf("") }
    var registerPhone by rememberSaveable { mutableStateOf("") }
    // Password drafts survive page changes in memory, but never enter saved instance state.
    var loginPassword by remember { mutableStateOf("") }
    var registerPassword by remember { mutableStateOf("") }
    var confirmation by remember { mutableStateOf("") }
    var demoResult by remember { mutableStateOf<AuthDestination?>(null) }

    BackHandler(enabled = destination == AuthDestination.Register && demoResult == null) {
        destination = AuthDestination.Login
    }
    when (destination) {
        AuthDestination.Login -> LoginScreen(
            phone = loginPhone, password = loginPassword,
            onPhoneChange = { loginPhone = it.trim() }, onPasswordChange = { loginPassword = it },
            onLogin = { demoResult = AuthDestination.Login },
            onRegister = { destination = AuthDestination.Register }, modifier = modifier,
        )
        AuthDestination.Register -> RegisterScreen(
            phone = registerPhone, password = registerPassword, confirmation = confirmation,
            onPhoneChange = { registerPhone = it.trim() }, onPasswordChange = { registerPassword = it },
            onConfirmationChange = { confirmation = it },
            onRegister = { demoResult = AuthDestination.Register },
            onBackToLogin = { destination = AuthDestination.Login }, modifier = modifier,
        )
    }
    demoResult?.let { result ->
        QDialog(
            title = stringResource(R.string.auth_demo_title),
            message = stringResource(if (result == AuthDestination.Register)
                R.string.auth_register_demo_message else R.string.auth_login_demo_message),
            confirmLabel = stringResource(if (result == AuthDestination.Register)
                R.string.auth_back_login else R.string.auth_understood),
            onConfirm = {
                demoResult = null
                if (result == AuthDestination.Register) {
                    loginPhone = registerPhone
                    loginPassword = ""
                    registerPassword = ""
                    confirmation = ""
                    destination = AuthDestination.Login
                } else {
                    loginPassword = ""
                }
            },
            onDismiss = { demoResult = null },
        )
    }
}
