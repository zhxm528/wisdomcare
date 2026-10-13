package com.wisdomcare.app.data

import com.wisdomcare.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/** 数据库凭据只保存在后端；客户端只携带当前构建对应的 API 地址。 */
object DatabaseGateway {
    val environment: String = BuildConfig.DATABASE_ENV

    suspend fun checkConnection() = withContext(Dispatchers.IO) {
        if (BuildConfig.API_BASE_URL.isBlank()) {
            throw IOException("当前为 UI 演示构建，未配置数据库服务地址")
        }
        val connection = URL("${BuildConfig.API_BASE_URL}/health/database")
            .openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000
            connection.instanceFollowRedirects = false
            connection.requestMethod = "GET"
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException("数据库服务暂不可用")
            }
            val payload = connection.inputStream.bufferedReader().use { JSONObject(it.readText()) }
            if (payload.optString("environment") != environment) {
                throw IOException("服务环境与当前应用不匹配")
            }
            if (payload.optString("status") != "ok") throw IOException("数据库连接检查失败")
        } finally {
            connection.disconnect()
        }
    }
}
