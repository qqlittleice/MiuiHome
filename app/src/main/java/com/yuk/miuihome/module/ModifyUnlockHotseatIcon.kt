package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class ModifyUnlockHotseatIcon {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("unlockIcons", false)) return
        "com.miui.home.launcher.DeviceConfig".hookBeforeMethod("getHotseatMaxCount") {
            it.result = 99
        }
    }
}