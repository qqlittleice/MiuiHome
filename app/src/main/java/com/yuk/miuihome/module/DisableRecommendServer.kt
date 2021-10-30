package com.yuk.miuihome.module

import com.yuk.miuihome.utils.ownSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class DisableRecommendServer {

    fun init() {
        if (ownSP.getBoolean("recommendServer", false)) {
            "com.miui.home.launcher.DeviceConfig".setReturnConstant(
                "isRecommendServerEnable",
                result = false
            )
        }
    }
}