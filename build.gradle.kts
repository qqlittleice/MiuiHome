buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        //noinspection AndroidGradlePluginVersion,GradleDependency
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath(kotlin("gradle-plugin", "1.6.0"))

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module settings.gradle.kts files
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}