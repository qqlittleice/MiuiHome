package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class DisableRecommendServer {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("recommendServer", false)) return
        "com.miui.home.launcher.DeviceConfig".hookBeforeMethod("isRecommendServerEnable") {
            it.result = false
        }
    }
}