package com.yuk.miuihome.module

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*
import de.robv.android.xposed.XposedBridge

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
                        launcherClass.hookAfterMethod("onCreate", Bundle::class.java
                        ) {
                            val activity = it.thisObject as Activity
                            launcherClass.hookAfterMethod("openFolder", folderInfo, View::class.java
                            ) {
                                blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true)
                            }
                            launcherClass.hookAfterMethod("closeFolder", Boolean::class.java
                            ) {
                                blurClass.callStaticMethod("fastBlur", 0.0f, activity.window, true)
                            }
                            val isFolderShowing = activity.callMethod("isFolderShowing") as Boolean
                            val navStubViewClass = "com.miui.home.recents.NavStubView".findClass()
                            val handler = Handler(Looper.getMainLooper())
                            val runnable = Runnable { blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true) }
                            navStubViewClass.hookAfterMethod("performAppToHome"
                            ) {
                                if (isFolderShowing) {
                                    handler.postDelayed(runnable, 1000)
                                    XposedBridge.log("Do it")
                                }
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