import com.android.build.api.component.analytics.AnalyticsEnabledApplicationVariant
import com.android.build.api.variant.impl.ApplicationVariantImpl

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 31
    val verCode = 4150
    val verName = "4.1.5"

    defaultConfig {
        applicationId = "com.yuk.miuihome"
        minSdk = 28
        targetSdk = 31
        versionCode = verCode
        versionName = verName
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                    "proguard-log.pro"
                )
            )
        }
    }

    androidComponents.onVariants { appVariant ->
        val variant: ApplicationVariantImpl =
            if (appVariant is ApplicationVariantImpl) appVariant
            else (appVariant as AnalyticsEnabledApplicationVariant).delegate as ApplicationVariantImpl
        variant.outputs.forEach {
            if  (appVariant.buildType == "release") it.outputFileName.set("MiuiHome-${verName}(${verCode})-release.apk") else it.outputFileName.set("MiuiHome-${verName}(${verCode})-debug.apk")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-beta02"
    }

    packagingOptions {
        resources {
            excludes += "META-INF/**"
            excludes += "kotlin/**"
            excludes += "**.bin"
        }
    }

    dependenciesInfo {
        includeInApk = false
    }
}

dependencies {
    val appCenterSdkVersion = "4.3.1"
    val composeVersion = "1.1.0-beta02"

    compileOnly("de.robv.android.xposed:api:82")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.compose.material3:material3:1.0.0-alpha01")

    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("com.microsoft.appcenter:appcenter-analytics:$appCenterSdkVersion")
    implementation("com.microsoft.appcenter:appcenter-crashes:$appCenterSdkVersion")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
}
