plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.github.igorergin.planner.core.common"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Core & Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.javax.inject)
    
    // Compose
    implementation(libs.androidx.compose.runtime)

    // DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}