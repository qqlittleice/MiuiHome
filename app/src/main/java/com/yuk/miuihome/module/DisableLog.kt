package com.yuk.miuihome.module

import com.yuk.miuihome.utils.ktx.setReturnConstant

class DisableLog {

    fun init() {
        "com.miui.home.launcher.MiuiHomeLog".setReturnConstant(
            "setDebugLogState",
            Boolean::class.java,
            result = false
        )

        "com.miui.home.launcher.MiuiHomeLog".setReturnConstant(
            "log",
            String::class.java,
            String::class.java,
            result = null
        )
        "com.xiaomi.onetrack.OneTrack".setReturnConstant(
            "isDisable",
            result = true
        )
    }
}