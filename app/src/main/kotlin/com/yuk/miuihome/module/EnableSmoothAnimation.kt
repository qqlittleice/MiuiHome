package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuihome.utils.OwnSP

class EnableSmoothAnimation {

    fun init() {
        if (OwnSP.ownSP.getBoolean("smoothAnimation", false) || !OwnSP.ownSP.getBoolean("simpleAnimation", false))
            findMethod("com.miui.home.launcher.common.Utilities") {
                name == "isUseSmoothAnimationEffect"
            }.hookReturnConstant(true)
        else
            findMethod("com.miui.home.launcher.common.Utilities") {
                name == "isUseSmoothAnimationEffect"
            }.hookReturnConstant(false)
    }
}
