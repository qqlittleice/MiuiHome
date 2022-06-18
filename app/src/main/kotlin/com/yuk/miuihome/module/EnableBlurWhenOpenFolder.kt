package com.yuk.miuihome.module

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*

class EnableBlurWhenOpenFolder {

    fun init() {
            if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                if (XposedInit().checkIsAlpha()) {
                    "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUserBlurWhenOpenFolder") {
                        it.result = false
                    }
                }
            }
            else {
                if (OwnSP.ownSP.getBoolean("blurWhenOpenFolder", false)) {
                    if (XposedInit().checkIsAlpha()) {
                        "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUserBlurWhenOpenFolder") {
                            it.result = true
                        }
                    }
                    else {
                        val blurClass = "com.miui.home.launcher.common.BlurUtils".findClass()
                        val folderInfo = "com.miui.home.launcher.FolderInfo".findClass()
                        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
                        val launcherStateClass = "com.miui.home.launcher.LauncherState".findClass()
                        launcherClass.hookAfterMethod("onCreate", Bundle::class.java) {
                            val activity = it.thisObject as Activity
                            var isFolderShowing = false
                            launcherClass.hookAfterMethod("isFolderShowing") { hookParam ->
                                isFolderShowing = hookParam.result as Boolean
                            }
                            launcherClass.hookAfterMethod("openFolder", folderInfo, View::class.java) {
                                blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true)
                            }
                            launcherClass.hookAfterMethod("closeFolder", Boolean::class.java) {
                                blurClass.callStaticMethod("fastBlur", 0.0f, activity.window, true, 300L)
                            }
                            blurClass.hookAfterMethod("fastBlurWhenStartOpenOrCloseApp", Boolean::class.java, launcherClass) { hookParam ->
                                if (isFolderShowing) hookParam.result = blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                            }
                            blurClass.hookAfterMethod("fastBlurWhenFinishOpenOrCloseApp", launcherClass) { hookParam ->
                                if (isFolderShowing) hookParam.result = blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                            }
                            blurClass.hookAfterMethod("fastBlurWhenExitRecents", launcherClass, launcherStateClass, Boolean::class.java) { hookParam ->
                                if (isFolderShowing) hookParam.result = blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                            }
                            launcherClass.hookAfterMethod("onGesturePerformAppToHome") {
                                if (isFolderShowing) blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                            }
                        }
                    }
                } else {
                    if (XposedInit().checkIsAlpha())
                        "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUserBlurWhenOpenFolder") {
                            it.result = false
                        }
                }
            }
    }
}