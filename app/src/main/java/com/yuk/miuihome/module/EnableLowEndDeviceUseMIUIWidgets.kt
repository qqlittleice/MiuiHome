package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class EnableLowEndDeviceUseMIUIWidgets {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("useMIUIWidgets", false)) return
        "com.miui.home.launcher.MIUIWidgetUtil".hookBeforeMethod("isMIUIWidgetSupport") {
            it.result = true
        }
    }
}