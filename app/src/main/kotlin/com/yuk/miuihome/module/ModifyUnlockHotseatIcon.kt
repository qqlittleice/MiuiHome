package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuihome.utils.OwnSP

class ModifyUnlockHotseatIcon {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("unlockIcons", false)) return
        findMethod("com.miui.home.launcher.DeviceConfig") {
            name == "getHotseatMaxCount"
        }.hookReturnConstant(88)
    }
}