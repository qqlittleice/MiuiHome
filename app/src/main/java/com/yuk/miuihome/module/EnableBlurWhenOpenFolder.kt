package com.yuk.miuihome.module

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.*

class EnableBlurWhenOpenFolder {

    fun init() {
            if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                if (XposedInit().checkAlpha()) {
                    "com.miui.home.launcher.common.BlurUtils".setReturnConstant("isUserBlurWhenOpenFolder", result = false)
                }
            }
            else {
                if (OwnSP.ownSP.getBoolean("blurWhenOpenFolder", false)) {
                    if (XposedInit().checkAlpha()) {
                        "com.miui.home.launcher.common.BlurUtils".setReturnConstant("isUserBlurWhenOpenFolder", result = true)
                    }
                    else {
                        val blurClass = "com.miui.home.launcher.common.BlurUtils".findClass()
                        val folderInfo = "com.miui.home.launcher.FolderInfo".findClass()
                        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
                        launcherClass.hookAfterMethod("onCreate", Bundle::class.java
                        ) {
                            val activity = it.thisObject as Activity
                            launcherClass.hookAfterMethod("openFolder", folderInfo,View::class.java
                            ) {
                                blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true)
                            }
                            launcherClass.hookAfterMethod("closeFolder", Boolean::class.java
                            ) {
                                blurClass.callStaticMethod("fastBlur", 0.0f, activity.window, true)
                            }
                        }
                    }
                } else {
                    if (XposedInit().checkAlpha())
                        "com.miui.home.launcher.common.BlurUtils".setReturnConstant("isUserBlurWhenOpenFolder", result = false)
                }
            }
    }
}