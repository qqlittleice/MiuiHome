package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.ktx.setReturnConstant

class EnableBlurWhenOpenFolder {

    fun init() {

        if (OwnSP.ownSP.getBoolean("blurWhenOpenFolder", false)) {
            "com.miui.home.launcher.common.BlurUtils".setReturnConstant("isUserBlurWhenOpenFolder", result = true)
        } else {
            "com.miui.home.launcher.common.BlurUtils".setReturnConstant("isUserBlurWhenOpenFolder", result = false)
        }
    }

}