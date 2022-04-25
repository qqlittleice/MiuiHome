package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuihome.utils.OwnSP

class EnableSimpleAnimation {

    fun init() {
        if (OwnSP.ownSP.getBoolean("simpleAnimation", false))
            findMethod("com.miui.home.launcher.common.DeviceLevelUtils") {
                name == "isUseSimpleAnim"
            }.hookReturnConstant(true)
        else
            findMethod("com.miui.home.launcher.common.DeviceLevelUtils") {
                name == "isUseSimpleAnim"
            }.hookReturnConstant(false)
    }
}