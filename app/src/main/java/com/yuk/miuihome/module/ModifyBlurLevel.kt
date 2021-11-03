package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class ModifyBlurLevel {

    fun init() {
        val string = ownSP.getString("blurLevel", "")
        if (ownSP.getBoolean("simpleAnimation", false)) {
            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("getBlurType") {
                it.result = 0
            }
            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUseCompleteBlurOnDev") {
                it.result = false
            }
        } else {
            if (string == "") return
            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("getBlurType") {
                when (string) {
                    "COMPLETE" -> {
                        it.result = 2
                    }
                    "SIMPLE" -> {
                        it.result = 1
                    }
                    "NONE" -> {
                        it.result = 0
                    }
                }
            }
            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUseCompleteBlurOnDev") {
                when (string) {
                    "TEST" -> {
                        it.result = true
                    }
                }
            }
        }
    }
}