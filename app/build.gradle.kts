import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 32
    buildToolsVersion = "32.0.0"
    defaultConfig {
        applicationId = "com.yuk.miuihome"
        minSdk = 29
        targetSdk = 32
        versionCode = getTimeStamp()
        versionName = "4.2.0" + (getGitHeadRefsSuffix(rootProject))
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
        applicationVariants.all {
            outputs.all {
                (this as BaseVariantOutputImpl).outputFileName = "MiuiHome-$versionName($versionCode)-$name.apk"
            }
        }
    }
    androidResources.additionalParameters("--allow-reserved-package-id", "--package-id", "0x64")
}
fun getGitHeadRefsSuffix(project: Project): String {
        // .git/HEAD描述当前目录所指向的分支信息，内容示例："ref: refs/heads/master\n"
        val headFile = File(project.rootProject.projectDir, ".git" + File.separator + "HEAD")
        if (headFile.exists()) {
            val string: String = headFile.readText(Charsets.UTF_8)
            val string1 = string.replace(Regex("""ref:|\s"""), "")
            val result = if (string1.isNotBlank() && string1.contains('/')) {
                val refFilePath = ".git" + File.separator + string1
                // 根据HEAD读取当前指向的hash值，路径示例为：".git/refs/heads/master"
                val refFile = File(project.rootProject.projectDir, refFilePath)
                // 索引文件内容为hash值+"\n"，
                // 示例："90312cd9157587d11779ed7be776e3220050b308\n"
                refFile.readText(Charsets.UTF_8).replace(Regex("""\s"""), "").subSequence(0, 7)
            } else {
                string.substring(0, 7)
            }
            println("commit_id: $result")
            return ".$result"
        } else {
            println("WARN: .git/HEAD does NOT exist")
            return ""
        }
    }

fun getTimeStamp(): Int {
        return (System.currentTimeMillis() / 1000L).toInt()
    }
dependencies {
    compileOnly("de.robv.android.xposed:api:82")
    implementation("com.microsoft.appcenter:appcenter-crashes:4.4.2")
    implementation("com.microsoft.appcenter:appcenter-analytics:4.4.2")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.3")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
}
