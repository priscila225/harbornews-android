plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.junit5)
}

android {
    namespace = "com.harbornews.app"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.harbornews.app"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":feature-news"))
    implementation(project(":feature-weather"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.work.runtime)

    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.engine)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit5)
}
