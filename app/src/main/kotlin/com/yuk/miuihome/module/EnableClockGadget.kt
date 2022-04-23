package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class EnableClockGadget {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("clockGadget", false)) return
        "com.miui.home.launcher.Workspace".hookBeforeMethod(
            "isScreenHasClockGadget",
            Long::class.java
        ) { it.result = false }
    }
}