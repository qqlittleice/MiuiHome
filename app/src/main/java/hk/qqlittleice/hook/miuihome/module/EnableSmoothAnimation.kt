package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.ktx.setReturnConstant

class EnableSmoothAnimation {

    fun init() {

        if (OwnSP.ownSP.getBoolean("smoothAnimation", false)) {
            "com.miui.home.launcher.common.Utilities".setReturnConstant("isUseSmoothAnimationEffect", result = true)
        } else {
            "com.miui.home.launcher.common.Utilities".setReturnConstant("isUseSmoothAnimationEffect", result = false)
        }
    }

}