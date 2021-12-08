package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class EnableLowEndDeviceUseMIUIWidgets {

    fun init() {
        if (OwnSP.ownSP.getBoolean("useMIUIWidgets", false)) {
            "com.miui.home.launcher.MIUIWidgetUtil".setReturnConstant(
                "isMIUIWidgetSupport",
                result = true
            )
        }
    }
}