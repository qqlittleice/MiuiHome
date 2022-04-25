package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuihome.utils.OwnSP

class EnableRecentsViewHorizontal {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("horizontal", false)) return
        findMethod("com.miui.home.launcher.DeviceConfig") {
            name == "isKeepRecentsViewPortrait"
        }.hookReturnConstant(false)
    }
}