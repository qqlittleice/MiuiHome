package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.ktx.setReturnConstant

class EnableHideStatusBarWhenEnterRecents {

    fun init() {

        if (OwnSP.ownSP.getBoolean("hideStatusBar", false)){
            "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant("isHideStatusBarWhenEnterRecents", result = true)
        } else {
            "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant("isHideStatusBarWhenEnterRecents", result = false)
        }
    }

}