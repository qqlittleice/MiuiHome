package com.yuk.miuihome.module

import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class EnableSmoothAnimation {

    fun init() {
        if (XposedInit().checkVersionCode() >= 427004733L) return
        if (OwnSP.ownSP.getBoolean("simpleAnimation", false))
            "com.miui.home.launcher.common.Utilities".hookBeforeMethod("isUseSmoothAnimationEffect") {
                it.result = false
            }
        else {
            if (OwnSP.ownSP.getBoolean("smoothAnimation", false))
                "com.miui.home.launcher.common.Utilities".hookBeforeMethod("isUseSmoothAnimationEffect") {
                    it.result = true
                }
            else
                "com.miui.home.launcher.common.Utilities".hookBeforeMethod("isUseSmoothAnimationEffect") {
                    it.result = false
                }
        }
    }
}