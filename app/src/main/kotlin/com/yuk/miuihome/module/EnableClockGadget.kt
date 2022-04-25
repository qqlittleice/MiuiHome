package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuihome.utils.OwnSP

class EnableClockGadget {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("clockGadget", false)) return
        findMethod("com.miui.home.launcher.Workspace") {
            name == "isScreenHasClockGadget" && parameterTypes[0] == Long::class.java
        }.hookReturnConstant(false)
    }
}