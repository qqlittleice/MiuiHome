package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class EnableHideStatusBarWhenEnterRecents {

    fun init() {
        if (OwnSP.ownSP.getBoolean("hideStatusBar", false)) {
            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod(
                "isHideStatusBarWhenEnterRecents"
            ) {
                it.result = true
            }
            "com.miui.home.launcher.DeviceConfig".hookBeforeMethod(
                "keepStatusBarShowingForBetterPerformance"
            ) {
                it.result = false
            }
        } else {
            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod(
                "isHideStatusBarWhenEnterRecents"
            ) {
                it.result = false
            }
        }
    }
}