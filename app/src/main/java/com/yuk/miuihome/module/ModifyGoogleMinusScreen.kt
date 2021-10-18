package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class ModifyGoogleMinusScreen {

    fun init() {
        if (OwnSP.ownSP.getBoolean("googleMinusScreen", false)) {
            "com.miui.home.launcher.DeviceConfig".setReturnConstant(
                "isUseGoogleMinusScreen",
                result = true
            )
        }
    }
}