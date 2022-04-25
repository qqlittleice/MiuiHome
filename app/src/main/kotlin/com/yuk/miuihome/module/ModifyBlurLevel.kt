package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuihome.utils.OwnSP

class ModifyBlurLevel : BaseClassAndMethodCheck {

    companion object {
        var checked = false
    }

    fun init() {
        val blurLevel = OwnSP.ownSP.getString("blurLevel", "SimpleBlur")
        if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) {
            findMethod("com.miui.home.launcher.common.BlurUtils") {
                name == "getBlurType"
            }.hookReturnConstant(0)
            findMethod("com.miui.home.launcher.common.BlurUtils") {
                name == "isUseCompleteBlurOnDev"
            }.hookReturnConstant(false)
        } else {
            findMethod("com.miui.home.launcher.common.BlurUtils") {
                name == "getBlurType"
            }.hookBefore {
                when (blurLevel) {
                    "CompleteBlur" -> it.result = 2
                    "SimpleBlur" -> it.result = 1
                    "NoneBlur" -> it.result = 0
                }
            }
            findMethod("com.miui.home.launcher.common.BlurUtils") {
                name == "isUseCompleteBlurOnDev"
            }.hookBefore {
                when (blurLevel) {
                    "TestBlur" -> it.result = true
                }
            }
            runWithChecked {
                checked = true
                findMethod("com.miui.home.launcher.common.BlurUtils") {
                    name == "isUseBasicBlur"
                }.hookBefore {
                    when (blurLevel) {
                        "BasicBlur" -> it.result = true
                    }
                }
            }
        }
    }

    override fun classAndMethodList(): ArrayList<String> = arrayListOf("com.miui.home.launcher.common.BlurUtils", "isUseBasicBlur")
}