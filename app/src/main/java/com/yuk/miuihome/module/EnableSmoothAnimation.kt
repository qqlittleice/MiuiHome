package com.yuk.miuihome.module

import com.yuk.miuihome.utils.ownSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class EnableSmoothAnimation {

    fun init() {
        if (ownSP.getBoolean("smoothAnimation", false)) {
            "com.miui.home.launcher.common.Utilities".setReturnConstant(
                "isUseSmoothAnimationEffect",
                result = true
            )
        } else {
            "com.miui.home.launcher.common.Utilities".setReturnConstant(
                "isUseSmoothAnimationEffect",
                result = false
            )
        }
    }
}