package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class ModifyUnlockHotseatIcon {

    fun init() {
        if (OwnSP.ownSP.getBoolean("unlockIcons", false)) {
            "com.miui.home.launcher.DeviceConfig".setReturnConstant(
                "getHotseatMaxCount",
                result = 99
            )
        }
    }
}