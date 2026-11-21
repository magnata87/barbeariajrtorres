plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.juniortorres.launcher"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.juniortorres.launcher"
        minSdk = 23
        targetSdk = 28
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation("com.google.zxing:core:3.5.3")
}
