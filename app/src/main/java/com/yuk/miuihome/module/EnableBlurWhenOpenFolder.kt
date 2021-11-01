package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.getMethodByClassOrObject
import com.yuk.miuihome.HomeContext.isAlpha
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.*

class EnableBlurWhenOpenFolder {

    fun init() {
        if (ownSP.getBoolean("simpleAnimation", false)) {
            "com.miui.home.launcher.common.BlurUtils".setReturnConstant(
                "isUserBlurWhenOpenFolder",
                result = false
            )
        } else {
            if (ownSP.getBoolean("blurWhenOpenFolder", false)) {
                if (isAlpha) {
                    "com.miui.home.launcher.common.BlurUtils".setReturnConstant(
                        "isUserBlurWhenOpenFolder",
                        result = false
                    )
                } else {
                    "com.miui.home.launcher.Launcher".hookBeforeMethod(
                        "isShouldBlur"
                    ) {
                        it.thisObject.apply {
                            val a = callMethod("isInNormalEditing") as Boolean
                            val b = callMethod("isFolderShowing") as Boolean
                            it.result = a || b
                        }
                    }
                }
            } else {
                if (isAlpha) {
                    "com.miui.home.launcher.common.BlurUtils".setReturnConstant(
                        "isUserBlurWhenOpenFolder",
                        result = false
                    )
                }
            }
        }
    }
}