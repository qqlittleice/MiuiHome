package com.yuk.miuihome.module

import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.setReturnConstant

class EnableLowEndDeviceUseMIUIWidgets {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("useMIUIWidgets", false)) return
        "com.miui.home.launcher.MIUIWidgetUtil".setReturnConstant("isMIUIWidgetSupport", result = true)
    }
}