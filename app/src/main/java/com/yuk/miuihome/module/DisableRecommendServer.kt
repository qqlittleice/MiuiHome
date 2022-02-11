package com.yuk.miuihome.module

import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.setReturnConstant

class DisableRecommendServer {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("recommendServer", false)) return
        "com.miui.home.launcher.DeviceConfig".setReturnConstant("isRecommendServerEnable", result = false)
    }
}