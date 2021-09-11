package com.yuk.miuihome.module


import android.view.View
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import de.robv.android.xposed.XposedHelpers
import kotlin.concurrent.thread

class TestCode {
    fun init() {
        "com.miui.home.launcher.Launcher".hookAfterMethod(
            "launch",
            "com.miui.home.launcher.ShortcutInfo",
            View::class.java
        ) {
            val mHasLaunchedAppFromFolder =
                XposedHelpers.getBooleanField(it.thisObject, "mHasLaunchedAppFromFolder")
            if (mHasLaunchedAppFromFolder) {
                XposedHelpers.callMethod(
                    it.thisObject,
                    "closeFolder"
                )
            }
        }
    }
}

