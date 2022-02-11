package com.yuk.miuihome.module

import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.setReturnConstant

class EnableClockGadget {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("clockGadget", false)) return
        "com.miui.home.launcher.Workspace".setReturnConstant("isScreenHasClockGadget", Long::class.java, result = false)
    }
}