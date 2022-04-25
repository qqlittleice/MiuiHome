package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuihome.utils.OwnSP

class EnableHideStatusBarWhenEnterRecents {

    fun init() {
        if (OwnSP.ownSP.getBoolean("hideStatusBar", false)) {
            findMethod("com.miui.home.launcher.common.DeviceLevelUtils") {
                name == "isHideStatusBarWhenEnterRecents"
            }.hookReturnConstant(true)
            findMethod("com.miui.home.launcher.DeviceConfig") {
                name == "keepStatusBarShowingForBetterPerformance"
            }.hookReturnConstant(false)
        } else {
            findMethod("com.miui.home.launcher.common.DeviceLevelUtils") {
                name == "isHideStatusBarWhenEnterRecents"
            }.hookReturnConstant(false)
        }
    }
}