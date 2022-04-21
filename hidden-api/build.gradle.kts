plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = 32
    namespace = "com.android.internal"
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
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.majorVersion
    }
}

dependencies {
}