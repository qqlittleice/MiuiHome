package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class ModifyBlurLevel {

    fun init() {

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
    }

}