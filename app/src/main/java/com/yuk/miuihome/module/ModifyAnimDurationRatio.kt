package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class ModifyAnimDurationRatio {

    fun init() {
        val value = ownSP.getFloat("animationLevel", -1f)
        if (value == -1f) return
        "com.miui.home.recents.TransitionAnimDurationHelper".setReturnConstant(
            "getAnimDurationRatio",
            result = value
        )
    }
}