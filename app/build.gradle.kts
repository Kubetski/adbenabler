plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

android {
    namespace = "com.example.adbenabler"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.adbenabler"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
}
