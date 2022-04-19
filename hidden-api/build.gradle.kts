plugins {
    id("com.android.library")
}

android {
    compileSdk = 32
    buildToolsVersion = "32.1.0-rc1"
    defaultConfig {
        minSdk = 29
        targetSdk = 32
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
}