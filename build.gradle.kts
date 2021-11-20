buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        //noinspection AndroidGradlePluginVersion,GradleDependency
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath(kotlin("gradle-plugin", "1.5.31"))

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module settings.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://api.xposed.info")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}