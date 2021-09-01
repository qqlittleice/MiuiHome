package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class EnableBlurWhenOpenFolder {

    fun init() {
        if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
            "com.miui.home.launcher.common.BlurUtils".setReturnConstant("isUserBlurWhenOpenFolder", result = false)
        } else {
            if (OwnSP.ownSP.getBoolean("blurWhenOpenFolder", false)) {
                "com.miui.home.launcher.common.BlurUtils".setReturnConstant("isUserBlurWhenOpenFolder", result = true)
            } else {
                "com.miui.home.launcher.common.BlurUtils".setReturnConstant("isUserBlurWhenOpenFolder", result = false)
            }
        }
    }
}