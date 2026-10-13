import java.util.Properties
import java.net.URI
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

val localSettings = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}
fun apiUrl(environment: String, fallback: String): String =
    providers.gradleProperty("wisdomcare.api.$environment")
        .orElse(providers.environmentVariable("WISDOMCARE_API_${environment.uppercase()}"))
        .getOrElse(localSettings.getProperty("wisdomcare.api.$environment", fallback)).trim().trimEnd('/')

val devApiUrl = apiUrl("dev", "http://10.0.2.2:3100")
val prodApiUrl = apiUrl("prod", "")
fun stringLiteral(value: String) = "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\""

@DisableCachingByDefault(because = "Validates configuration without producing outputs")
abstract class ValidateProdApi : DefaultTask() {
    @get:Input
    abstract val apiUrl: Property<String>

    @TaskAction
    fun validate() {
        val configuredUrl = apiUrl.get()
        if (configuredUrl.isBlank()) {
            logger.lifecycle("No production API configured: building a UI-only release with backend access unavailable.")
            return
        }
        val uri = runCatching { URI(configuredUrl) }.getOrNull()
        check(uri?.scheme == "https" && !uri.host.isNullOrBlank() && uri.userInfo == null &&
            uri.query == null && uri.fragment == null) {
            "Configured wisdomcare.api.prod must be a valid HTTPS URL without credentials, query, or fragment."
        }
    }
}
tasks.configureEach {
    if (name == "preReleaseBuild") dependsOn(validateProdApi)
}

android {
    namespace = "com.wisdomcare.app"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.wisdomcare.app"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "DATABASE_ENV", "\"dev\"")
            buildConfigField("String", "API_BASE_URL", stringLiteral(devApiUrl))
        }
        release {
            buildConfigField("String", "DATABASE_ENV", "\"prod\"")
            buildConfigField("String", "API_BASE_URL", stringLiteral(prodApiUrl))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

val validateProdApi by tasks.registering(ValidateProdApi::class) {
    apiUrl.set(prodApiUrl)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
