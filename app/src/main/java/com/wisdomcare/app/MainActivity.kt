package com.wisdomcare.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.wisdomcare.app.data.DatabaseGateway
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wisdomcare.app.ui.theme.WisdomcareTheme
import com.wisdomcare.app.ui.theme.QMDJTheme
import com.wisdomcare.app.feature.auth.AuthNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QMDJTheme {
                AuthNavigation()
            }
        }
    }
}

@Composable
fun DatabaseConnectionPanel(modifier: Modifier = Modifier) {
    var checking by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("尚未检查连接") }
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("WisdomCare")
        Text("当前环境：${DatabaseGateway.environment}")
        Text(status)
        Button(enabled = !checking, onClick = {
            scope.launch {
                checking = true
                status = "正在检查…"
                try {
                    DatabaseGateway.checkConnection()
                    status = "数据库连接正常"
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (_: Exception) {
                    status = "连接失败，请检查服务地址、网络和服务环境"
                } finally {
                    checking = false
                }
            }
        }) { Text("检查数据库连接") }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WisdomcareTheme {
        Greeting("Android")
    }
}
