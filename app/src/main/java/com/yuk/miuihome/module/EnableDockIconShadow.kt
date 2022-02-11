package com.yuk.miuihome.module

import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.setReturnConstant


class EnableDockIconShadow {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("isEnableIconShadow", false)) return
        "com.miui.home.launcher.Launcher".setReturnConstant("isEnableIconShadow", result = true)
    }
}