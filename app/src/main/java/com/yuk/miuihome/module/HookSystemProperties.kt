package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class HookSystemProperties {

    fun init() {
        "android.os.SystemProperties".hookBeforeMethod(
            "getBoolean",
            String::class.java,
            Boolean::class.java
        ) {
            if (it.args[0] == "ro.miui.backdrop_sampling_enabled") {
                it.result = true
            }
        }
        if (OwnSP.ownSP.getBoolean("lowEndAnim", false)) {
            "android.os.SystemProperties".hookBeforeMethod(
                "getBoolean",
                String::class.java,
                Boolean::class.java
            ) {
                if (it.args[0] == "ro.config.low_ram.threshold_gb") {
                    it.result = false
                }
            }
        }
    }
}