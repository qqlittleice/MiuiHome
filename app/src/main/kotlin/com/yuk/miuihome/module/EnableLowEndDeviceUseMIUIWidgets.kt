package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuihome.utils.OwnSP

class EnableLowEndDeviceUseMIUIWidgets {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("useMIUIWidgets", false)) return
        findMethod("com.miui.home.launcher.MIUIWidgetUtil") {
            name == "isMIUIWidgetSupport"
        }.hookReturnConstant(true)
    }
}