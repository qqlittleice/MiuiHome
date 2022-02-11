package com.yuk.miuihome.module

import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.setReturnConstant

class EnableSimpleAnimation {

    fun init() {
        if (OwnSP.ownSP.getBoolean("simpleAnimation", false))
            "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant("isUseSimpleAnim", result = true)
        else
            "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant("isUseSimpleAnim", result = false)
    }
}