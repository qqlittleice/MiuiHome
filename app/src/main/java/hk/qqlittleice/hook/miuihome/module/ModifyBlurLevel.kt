package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.utils.LogUtil
import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.ktx.hookBeforeMethod

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