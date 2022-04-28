package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuihome.utils.OwnSP

class EnableDockIconShadow {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("isEnableIconShadow", false)) return
        findMethod("com.miui.home.launcher.Launcher") {
            name == "isEnableIconShadow"
        }.hookReturnConstant(true)
    }
}