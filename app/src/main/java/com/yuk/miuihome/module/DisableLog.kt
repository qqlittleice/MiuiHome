package com.yuk.miuihome.module

import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.utils.ktx.hookBeforeMethod
import de.robv.android.xposed.XposedBridge

class DisableLog {

    fun init() {
        try {
            if (XposedInit().checkVersionCode() <= 426004312L) "com.miui.home.launcher.MiuiHomeLog".hookBeforeMethod(
                "setDebugLogState",
                Boolean::class.java
            ) {
                it.result = false
            }
            "com.miui.home.launcher.MiuiHomeLog".hookBeforeMethod(
                "log",
                String::class.java,
                String::class.java
            ) {
                it.result = null
            }
            "com.xiaomi.onetrack.OneTrack".hookBeforeMethod("isDisable") {
                it.result = true
            }
        } catch (e: Throwable) {
            XposedBridge.log(e)
        }
    }
}