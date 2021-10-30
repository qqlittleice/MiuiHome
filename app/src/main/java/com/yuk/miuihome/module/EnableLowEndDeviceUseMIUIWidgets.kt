package com.yuk.miuihome.module

import com.yuk.miuihome.utils.ownSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class EnableLowEndDeviceUseMIUIWidgets {

    fun init() {
        if (ownSP.getBoolean("useMIUIWidgets", false)) {
            "com.miui.home.launcher.MIUIWidgetUtil".setReturnConstant(
                "isMIUIWidgetSupport",
                result = true
            )
        }
    }
}