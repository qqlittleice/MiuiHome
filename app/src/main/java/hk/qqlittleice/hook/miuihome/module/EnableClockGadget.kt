package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.ktx.setReturnConstant

class EnableClockGadget {

    fun init() {
        if (OwnSP.ownSP.getBoolean("clockGadget", false)) {
            "com.miui.home.launcher.Workspace".setReturnConstant("isScreenHasClockGadget", Long::class.java, result = false)
        }
    }

}