import com.android.build.api.component.analytics.AnalyticsEnabledApplicationVariant
import com.android.build.api.variant.impl.ApplicationVariantImpl

plugins {
    id("com.android.application")
    id("kotlin-android")
}

val verCode = 4185
val verName = "4.1.8"

android {
    compileSdk = 32
    buildToolsVersion = "32.0.0"
    defaultConfig {
        applicationId = "com.yuk.miuihome"
        minSdk = 28
        targetSdk = 32
        versionCode = verCode
        versionName = verName
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro", "proguard-log.pro"))
        }
        create("noResHook") {
            initWith(getByName("release"))
        }
    }
    sourceSets.getByName("main") {
        java.setSrcDirs(java.srcDirs + File(projectDir, "src/noResHook/java"))
    }
    androidComponents.onVariants { appVariant ->
        val variant: ApplicationVariantImpl =
            if (appVariant is ApplicationVariantImpl) appVariant
            else (appVariant as AnalyticsEnabledApplicationVariant).delegate as ApplicationVariantImpl
        variant.outputs.forEach {
            when (appVariant.buildType) {
                "release" -> it.outputFileName.set("MiuiHome-${verName}(${verCode})-Release.apk")
                "debug" -> it.outputFileName.set("MiuiHome-${verName}(${verCode})-Debug.apk")
                "noResHook" -> it.outputFileName.set("MiuiHome-${verName}(${verCode})-NoResHook.apk")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/**"
            excludes += "/kotlin/**"
            excludes += "/*.txt"
            excludes += "/*.bin"
            excludes += "/okhttp3/**"
        }
        dex {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    compileOnly("de.robv.android.xposed:api:82")
    implementation("com.microsoft.appcenter:appcenter-crashes:4.4.1")
    implementation("com.microsoft.appcenter:appcenter-analytics:4.4.1")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.3")
}
