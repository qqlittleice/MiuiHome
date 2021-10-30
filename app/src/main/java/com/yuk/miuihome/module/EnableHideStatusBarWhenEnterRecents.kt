package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class EnableHideStatusBarWhenEnterRecents {

    fun init() {
        if (ownSP.getBoolean("hideStatusBar", false)) {
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