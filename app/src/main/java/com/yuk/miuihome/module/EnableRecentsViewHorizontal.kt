package com.yuk.miuihome.module

import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.setReturnConstant

class EnableRecentsViewHorizontal {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("horizontal", false)) return
        "com.miui.home.launcher.DeviceConfig".setReturnConstant("isKeepRecentsViewPortrait", result = false)
    }
}