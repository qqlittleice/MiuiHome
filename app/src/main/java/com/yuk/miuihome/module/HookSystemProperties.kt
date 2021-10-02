package com.yuk.miuihome.module

import com.yuk.miuihome.utils.ktx.hookBeforeMethod
import de.robv.android.xposed.XposedBridge

class HookSystemProperties {

    fun init() {
        "android.os.SystemProperties".hookBeforeMethod("getBoolean", String::class.java, Boolean::class.java) {
            if (it.args[0] == "ro.miui.backdrop_sampling_enabled") {
                it.result = true
                XposedBridge.log("ro.miui.backdrop_sampling_enabled hooked!")
            }
        }
    }

}