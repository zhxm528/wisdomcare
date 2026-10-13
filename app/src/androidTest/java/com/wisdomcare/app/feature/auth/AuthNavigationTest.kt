package com.wisdomcare.app.feature.auth

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wisdomcare.app.ui.theme.QMDJTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthNavigationTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    private fun launch() = compose.setContent { QMDJTheme { AuthNavigation() } }

    private fun click(tag: String) {
        compose.onNodeWithTag(tag).performScrollTo().performClick()
    }

    private fun enter(tag: String, text: String) {
        compose.onNodeWithTag(tag).performScrollTo().performTextInput(text)
    }

    @Test
    fun invalidLoginShowsErrorsAndKeyboardSubmissionCanRecover() {
        launch()
        click("login_submit")
        compose.onNodeWithTag("login_phone").assertIsFocused()
        compose.onNodeWithText("请输入手机号。").assertExists()
        enter("login_phone", "13800138000")
        enter("login_password", "12345678")
        compose.onNodeWithTag("login_password").performImeAction()
        compose.onNodeWithText("页面演示").assertIsDisplayed()
        compose.onNodeWithText("知道了").performClick()
        compose.onNodeWithTag("login_password").assertTextEquals("")
    }

    @Test
    fun systemBackAndVisibleBackPreserveDraftsWithoutGrowingTheStack() {
        launch()
        enter("login_phone", "13800138000")
        enter("login_password", "12345678")
        click("login_navigate")
        enter("register_phone", "13900139000")
        enter("register_password", "abcdefgh")
        compose.runOnUiThread { compose.activity.onBackPressedDispatcher.onBackPressed() }
        compose.onNodeWithTag("login_title").assertIsDisplayed()
        compose.onNodeWithTag("login_phone").assertTextEquals("13800138000")
        click("login_navigate")
        compose.onNodeWithTag("register_phone").assertTextEquals("13900139000")
        click("register_password_visibility")
        compose.onNodeWithTag("register_password").assertTextEquals("abcdefgh")
        click("register_navigate")
        compose.onNodeWithTag("login_title").assertIsDisplayed()
    }

    @Test
    fun registrationMismatchRetainsInputAndDemoReturnsPhoneToLogin() {
        launch()
        click("login_navigate")
        enter("register_phone", "13900139000")
        enter("register_password", "abcdefgh")
        enter("register_confirmation", "abcdxxxx")
        click("register_submit")
        compose.onNodeWithText("两次密码不一致，请重新确认。").assertExists()
        compose.onNodeWithTag("register_confirmation").assertIsFocused()
        compose.onNodeWithTag("register_confirmation").performTextReplacement("abcdefgh")
        click("register_submit")
        compose.onNodeWithText("页面演示").assertIsDisplayed()
        compose.onNode(hasText("返回登录") and hasAnyAncestor(isDialog())).performClick()
        compose.onNodeWithTag("login_phone").assertTextEquals("13900139000")
        click("login_navigate")
        compose.onNodeWithTag("register_password").assertTextEquals("")
        compose.onNodeWithTag("register_confirmation").assertTextEquals("")
    }

    @Test
    fun restorationKeepsDestinationAndPhoneButDoesNotSavePasswords() {
        val restoration = StateRestorationTester(compose)
        restoration.setContent { QMDJTheme { AuthNavigation() } }
        click("login_navigate")
        enter("register_phone", "13900139000")
        enter("register_password", "abcdefgh")
        enter("register_confirmation", "abcdefgh")
        restoration.emulateSavedInstanceStateRestore()
        compose.onNodeWithTag("register_title").assertIsDisplayed()
        compose.onNodeWithTag("register_phone").assertTextEquals("13900139000")
        compose.onNodeWithTag("register_password").assertTextEquals("")
        compose.onNodeWithTag("register_confirmation").assertTextEquals("")
    }

    @Test
    fun narrowScreenAtTwoHundredPercentTextKeepsBothFormsUsable() {
        compose.setContent {
            val density = LocalDensity.current.density
            CompositionLocalProvider(LocalDensity provides Density(density, fontScale = 2f)) {
                QMDJTheme { AuthNavigation(Modifier.width(320.dp)) }
            }
        }
        compose.onNodeWithTag("login_phone").assertHeightIsAtLeast(60.dp)
        compose.onNodeWithTag("login_submit").assertHeightIsAtLeast(60.dp).assertWidthIsAtLeast(56.dp)
        enter("login_phone", "13800138000")
        enter("login_password", "12345678")
        click("login_submit")
        compose.onNodeWithText("知道了").assertIsDisplayed().performClick()
        click("login_navigate")
        enter("register_phone", "13900139000")
        enter("register_password", "abcdefgh")
        enter("register_confirmation", "abcdefgh")
        click("register_submit")
        compose.onNode(hasText("返回登录") and hasAnyAncestor(isDialog())).assertIsDisplayed().performClick()
        compose.onNodeWithTag("login_phone").assertTextEquals("13900139000")
    }
}
