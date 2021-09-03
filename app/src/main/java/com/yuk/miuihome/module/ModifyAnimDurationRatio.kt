package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class ModifyAnimDurationRatio {

    fun init() {

        val value = OwnSP.ownSP.getFloat("animationLevel", 233f)
        if (value != 233f) {
            "com.miui.home.recents.TransitionAnimDurationHelper".setReturnConstant(
                "getAnimDurationRatio",
                result = value
            )
        }
    }
}