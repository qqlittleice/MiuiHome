package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class EnableSmoothAnimation {

    fun init() {
        if (OwnSP.ownSP.getBoolean("simpleAnimation", false))
            "com.miui.home.launcher.common.Utilities".setReturnConstant("isUseSmoothAnimationEffect", result = false)
        else {
            if (OwnSP.ownSP.getBoolean("smoothAnimation", false))
                "com.miui.home.launcher.common.Utilities".setReturnConstant("isUseSmoothAnimationEffect", result = true)
            else
                "com.miui.home.launcher.common.Utilities".setReturnConstant("isUseSmoothAnimationEffect", result = false)
        }
    }
}