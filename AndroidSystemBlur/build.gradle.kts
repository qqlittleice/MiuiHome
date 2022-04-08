/*
 *
 *  * AndroidSystemBlur
 *  * Copyright (c) 2022 Zhenxiang Chen
 *  *
 *  * This program is free software; you can redistribute it and/or
 *  * modify it under the terms of the GNU General Public License
 *  * as published by the Free Software Foundation; either version 2
 *  * of the License, or (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program; if not, write to the Free Software
 *  * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 *
 */

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = 32
    buildToolsVersion = "32.0.0"
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
    compileOnly(project(":hidden-api"))
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.annotation:annotation:1.3.0")
    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
}