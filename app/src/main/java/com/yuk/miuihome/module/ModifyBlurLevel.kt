package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class ModifyBlurLevel {

    fun init() {
        if (OwnSP.ownSP.getBoolean("simpleAnimation", false) and OwnSP.ownSP.getBoolean(
                "testUser",
                false
            )
        ) {
            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("getBlurType") {
                it.result = 0
            }

            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUseCompleteBlurOnDev") {
                it.result = false
            }
        } else {
            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("getBlurType") {
                when (OwnSP.ownSP.getString("blurLevel", "")) {
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
                when (OwnSP.ownSP.getString("blurLevel", "")) {
                    "TEST" -> {
                        it.result = true
                    }
                }
            }

            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod("isLowLevelOrLiteDevice") {
                when (OwnSP.ownSP.getString("blurLevel", "")) {
                    "TEST" -> {
                        it.result = false
                    }
                    "COMPLETE" -> {
                        it.result = false
                    }
                    "SIMPLE" -> {
                        it.result = true
                    }
                    "NONE" -> {
                        it.result = true
                    }
                }
            }
        }
    }
}