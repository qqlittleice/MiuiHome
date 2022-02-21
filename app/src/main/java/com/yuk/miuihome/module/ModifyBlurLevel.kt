package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class ModifyBlurLevel : BaseClassAndMethodCheck {

    companion object {
        var checked = false
    }

    fun init() {
        val blurLevel = OwnSP.ownSP.getString("blurLevel", "SimpleBlur")
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
                    "CompleteBlur" -> it.result = 2
                    "SimpleBlur" -> it.result = 1
                    "NoneBlur" -> it.result = 0
                }
            }
            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUseCompleteBlurOnDev") {
                when (blurLevel) {
                    "TestBlur" -> it.result = true
                }
            }
            runWithChecked {
                checked = true
                "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUseBasicBlur") {
                    when (blurLevel) {
                        "BasicBlur" -> it.result = true
                    }
                }
            }
        }
    }

    override fun classAndMethodList(): ArrayList<String> = arrayListOf("com.miui.home.launcher.common.BlurUtils", "isUseBasicBlur")
}