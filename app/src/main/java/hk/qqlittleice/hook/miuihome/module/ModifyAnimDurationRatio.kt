package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.ktx.setReturnConstant

class ModifyAnimDurationRatio {

    fun init() {
        val value = OwnSP.ownSP.getFloat("animationLevel", 233f)
        if (value != 233f) {
            "com.miui.home.recents.TransitionAnimDurationHelper".setReturnConstant("getAnimDurationRatio", result = value)
        }
    }

}