package com.yuk.miuihome.module

import android.view.Window
import com.yuk.miuihome.HomeContext.isAlpha
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.*
import com.yuk.miuihome.HomeContext.activity

class EnableBlurWhenOpenFolder {

    fun init() {
        if (ownSP.getBoolean("simpleAnimation", false)) {
            "com.miui.home.launcher.common.BlurUtils".setReturnConstant(
                "isUserBlurWhenOpenFolder",
                result = false
            )
        } else {
            if (ownSP.getBoolean("blurWhenOpenFolder", false)) {
                "com.miui.home.launcher.common.BlurUtils".setReturnConstant(
                    "isUserBlurWhenOpenFolder",
                    result = true
                )
            } else {
                "com.miui.home.launcher.common.BlurUtils".setReturnConstant(
                    "isUserBlurWhenOpenFolder",
                    result = false
                )

            }
        }
    }
}