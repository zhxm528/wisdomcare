package com.wisdomcare.app.feature.auth

import com.wisdomcare.app.R

/** Local demo form rules only. No account lookup, authentication, or persistence. */
internal object AuthValidation {
    fun phoneError(phone: String): Int? = when {
        phone.isBlank() -> R.string.auth_phone_required
        !phone.matches(Regex("1[3-9][0-9]{9}")) -> R.string.auth_phone_invalid
        else -> null
    }

    fun passwordError(password: String): Int? = when {
        password.isEmpty() -> R.string.auth_password_required
        password.length < 8 -> R.string.auth_password_short
        else -> null
    }

    fun confirmationError(password: String, confirmation: String): Int? = when {
        confirmation.isEmpty() -> R.string.auth_confirmation_required
        confirmation != password -> R.string.auth_confirmation_mismatch
        else -> null
    }
}
