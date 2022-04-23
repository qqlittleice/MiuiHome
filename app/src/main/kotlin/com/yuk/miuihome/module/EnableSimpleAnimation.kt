package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class EnableSimpleAnimation {

    fun init() {
        if (OwnSP.ownSP.getBoolean("simpleAnimation", false))
            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod("isUseSimpleAnim") {
                it.result = true
            }
        else
            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod("isUseSimpleAnim") {
                it.result = false
            }
    }
}