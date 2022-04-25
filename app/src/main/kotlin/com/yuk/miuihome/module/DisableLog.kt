package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReplace
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuihome.XposedInit

class DisableLog {

    fun init() {
        try {
            if (XposedInit().checkVersionCode() <= 426004312L) {
                findMethod("com.miui.home.launcher.MiuiHomeLog") {
                    name == "setDebugLogState" && parameterTypes[0] == Boolean::class.java
                }.hookReturnConstant(false)
            }
            findMethod("com.miui.home.launcher.MiuiHomeLog") {
                name == "log" && parameterTypes[0] == String::class.java && parameterTypes[1] == String::class.java
            }.hookReplace { null }
            findMethod("com.xiaomi.onetrack.OneTrack") {
                name == "isDisable"
            }.hookReturnConstant(true)
        } catch (e: Throwable) {
            Log.ex(e)
        }
    }
}