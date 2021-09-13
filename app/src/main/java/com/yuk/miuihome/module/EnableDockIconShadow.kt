package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.setReturnConstant


class EnableDockIconShadow {

    fun init() {
        if (OwnSP.ownSP.getBoolean("isEnableIconShadow", false)) {
            "com.miui.home.launcher.Launcher".setReturnConstant(
                "isEnableIconShadow",
                result = true
            )
        }
    }
}