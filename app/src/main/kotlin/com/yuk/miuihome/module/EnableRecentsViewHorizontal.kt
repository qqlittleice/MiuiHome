package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class EnableRecentsViewHorizontal {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("horizontal", false)) return
        "com.miui.home.launcher.DeviceConfig".hookBeforeMethod("isKeepRecentsViewPortrait") {
            it.result = false
        }
    }
}