package com.wisdomcare.app.data

import com.wisdomcare.app.BuildConfig
import java.io.IOException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test

class DatabaseGatewayUiOnlyTest {
    @Test
    fun missingProductionAddressFailsBeforeCreatingANetworkConnection() {
        assumeTrue("Only applies to UI-only release builds", BuildConfig.API_BASE_URL.isBlank())
        val failure = runCatching {
            runBlocking { DatabaseGateway.checkConnection() }
        }.exceptionOrNull()

        assertTrue(failure is IOException)
        assertEquals("当前为 UI 演示构建，未配置数据库服务地址", failure?.message)
    }
}
