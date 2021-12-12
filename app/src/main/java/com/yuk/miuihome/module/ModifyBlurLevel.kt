package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class ModifyBlurLevel {

    fun init() {
        val blurLevel = OwnSP.ownSP.getFloat("blurLevel", 2f)
        if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("getBlurType") {
                it.result = 0
            }
            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUseCompleteBlurOnDev") {
                it.result = false
            }
        } else {
            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("getBlurType") {
                when (blurLevel) {
                    2f -> {
                        it.result = 2
                    }
                    1f -> {
                        it.result = 1
                    }
                    0f -> {
                        it.result = 0
                    }
                }
            }
            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUseCompleteBlurOnDev") {
                when (blurLevel) {
                    3f -> {
                        it.result = true
                    }
                }
            }
        }
    }
}