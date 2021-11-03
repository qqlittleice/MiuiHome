package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.*

class EnableBlurWhenOpenFolder : BaseClassAndMethodCheck {

    companion object {
        var checked = false
    }

    fun init() {
        runWithChecked {
            checked = true
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

    override fun classAndMethodList(): ArrayList<String> = arrayListOf(
        "com.miui.home.launcher.common.BlurUtils", "isUserBlurWhenOpenFolder"
    )
}