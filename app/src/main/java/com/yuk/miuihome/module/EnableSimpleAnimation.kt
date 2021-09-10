package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class EnableSimpleAnimation {

    fun init() {
        if (OwnSP.ownSP.getBoolean("testUser", false)) {
            if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
                "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant(
                    "isUseSimpleAnim",
                    result = true
                )
            } else {
                "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant(
                    "isUseSimpleAnim",
                    result = false
                )
            }
        }
    }
}