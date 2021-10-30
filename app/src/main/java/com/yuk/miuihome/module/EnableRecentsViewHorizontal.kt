package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class EnableRecentsViewHorizontal {

    fun init() {
        if (ownSP.getBoolean("horizontal", false)) {
            "com.miui.home.launcher.DeviceConfig".setReturnConstant(
                "isKeepRecentsViewPortrait",
                result = false
            )
        }
    }
}