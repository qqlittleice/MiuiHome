package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class EnableBlurWhenOpenFolder: BaseClassAndMethodCheck {

    companion object {
        var checked = false
    }

    fun init() {
        runWithChecked {
            checked = true
            if (OwnSP.ownSP.getBoolean("simpleAnimation", false) and OwnSP.ownSP.getBoolean(
                    "testUser",
                    false
                )
            ) {
                "com.miui.home.launcher.common.BlurUtils".setReturnConstant(
                    "isUserBlurWhenOpenFolder",
                    result = false
                )
            } else {
                if (OwnSP.ownSP.getBoolean("blurWhenOpenFolder", false)) {
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