package com.wisdomcare.app.feature.auth

import com.wisdomcare.app.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthValidationTest {
    @Test
    fun phoneRequiresElevenMainlandDigits() {
        assertEquals(R.string.auth_phone_required, AuthValidation.phoneError(" "))
        listOf("1380013800", "138001380000", "12800138000", "abcdefghijk", "１３８００１３８０００").forEach {
            assertEquals(R.string.auth_phone_invalid, AuthValidation.phoneError(it))
        }
        assertNull(AuthValidation.phoneError("13800138000"))
    }

    @Test
    fun passwordAndConfirmationHaveDistinctRecoveryMessages() {
        assertEquals(R.string.auth_password_required, AuthValidation.passwordError(""))
        assertEquals(R.string.auth_password_short, AuthValidation.passwordError("1234567"))
        assertNull(AuthValidation.passwordError("12345678"))
        assertEquals(R.string.auth_confirmation_required, AuthValidation.confirmationError("12345678", ""))
        assertEquals(R.string.auth_confirmation_mismatch, AuthValidation.confirmationError("12345678", "12345679"))
        assertNull(AuthValidation.confirmationError("12345678", "12345678"))
    }
}
