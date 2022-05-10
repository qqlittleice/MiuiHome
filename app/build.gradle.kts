import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 32
    namespace = "com.yuk.miuihome"
    defaultConfig {
        applicationId = "com.yuk.miuihome"
        minSdk = 29
        targetSdk = 32
        versionCode = 4300
        versionName = "4.3"
    }
    val properties = Properties()
    runCatching {
        properties.load(project.rootProject.file("local.properties").inputStream())
    }
    val keystorePath = properties.getProperty("KEYSTORE_PATH") ?: System.getenv("KEYSTORE_PATH")
    val keystorePwd = properties.getProperty("KEYSTORE_PASS") ?: System.getenv("KEYSTORE_PASS")
    val alias = properties.getProperty("KEY_ALIAS") ?: System.getenv("KEY_ALIAS")
    val pwd = properties.getProperty("KEY_PASSWORD") ?: System.getenv("KEY_PASSWORD")
    if (keystorePath != null) {
        signingConfigs {
            create("release") {
                storeFile = file(keystorePath)
                storePassword = keystorePwd
                keyAlias = alias
                keyPassword = pwd
                enableV3Signing = true
            }
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(listOf("proguard-rules.pro", "proguard-log.pro"))
            if (keystorePath != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        debug {
            if (keystorePath != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.majorVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/**"
            excludes += "/kotlin/**"
            excludes += "/okhttp3/**"
            excludes += "/*.txt"
            excludes += "/*.bin"
            excludes += "/*.json"
        }
        dex {
            useLegacyPackaging = true
        }
        applicationVariants.all {
            outputs.all {
                (this as BaseVariantOutputImpl).outputFileName = "MiuiHome-$versionName($versionCode)-$name.apk"
            }
        }
    }
    androidResources.additionalParameters("--allow-reserved-package-id", "--package-id", "0x64")
}

dependencies {
    //xposed api
    compileOnly("de.robv.android.xposed:api:82")
    //EzXHelper
    implementation("com.github.kyuubiran:EzXHelper:0.9.2")
    //recyclerview
    implementation("androidx.recyclerview:recyclerview:1.3.0-alpha02")
}
