package com.yuk.miuihome.module

import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.setReturnConstant

class ModifyUnlockHotseatIcon {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("unlockIcons", false)) return
        "com.miui.home.launcher.DeviceConfig".setReturnConstant("getHotseatMaxCount", result = 99)
    }
}