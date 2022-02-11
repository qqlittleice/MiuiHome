package com.yuk.miuihome.module

import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.setReturnConstant

class EnableHideStatusBarWhenEnterRecents {

    fun init() {
        if (OwnSP.ownSP.getBoolean("hideStatusBar", false)) {
            "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant(
                "isHideStatusBarWhenEnterRecents",
                result = true
            )
            "com.miui.home.launcher.DeviceConfig".setReturnConstant(
                "keepStatusBarShowingForBetterPerformance",
                result = false
            )
        } else {
            "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant(
                "isHideStatusBarWhenEnterRecents",
                result = false
            )
        }
    }
}