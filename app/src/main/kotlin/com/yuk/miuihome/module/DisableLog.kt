package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.Log
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookBeforeMethod
import com.yuk.miuihome.utils.ktx.replaceMethod

class DisableLog {

    fun init() {
        try {
            if (XposedInit().checkVersionCode() <= 426004312L) "com.miui.home.launcher.MiuiHomeLog".hookBeforeMethod(
                "setDebugLogState",
                Boolean::class.java
            ) {
                it.result = false
            }
            "com.miui.home.launcher.MiuiHomeLog".findClass().replaceMethod(
                "log",
                String::class.java,
                String::class.java
            ) {
                return@replaceMethod null
            }
            "com.xiaomi.onetrack.OneTrack".hookBeforeMethod("isDisable") {
                it.result = true
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
    }
}