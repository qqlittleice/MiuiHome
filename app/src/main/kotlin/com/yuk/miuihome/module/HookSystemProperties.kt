package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.yuk.miuihome.utils.OwnSP

class HookSystemProperties {

    fun init() {
        findMethod("android.os.SystemProperties") {
            name == "getBoolean" && parameterTypes[0] == String::class.java && parameterTypes[1] == Boolean::class.java
        }.hookBefore {
            if (it.args[0] == "ro.miui.backdrop_sampling_enabled") it.result = true
        }
        if (OwnSP.ownSP.getBoolean("lowEndAnim", false)) {
            findMethod("android.os.SystemProperties") {
                name == "getBoolean" && parameterTypes[0] == String::class.java && parameterTypes[1] == Boolean::class.java
            }.hookBefore {
                if (it.args[0] == "ro.config.low_ram.threshold_gb") it.result = false
            }
        }
    }
}