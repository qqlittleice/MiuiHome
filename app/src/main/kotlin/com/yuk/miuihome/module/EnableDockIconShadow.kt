package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod


class EnableDockIconShadow {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("isEnableIconShadow", false)) return
        "com.miui.home.launcher.Launcher".hookBeforeMethod("isEnableIconShadow") {
            it.result = true
        }
    }
}