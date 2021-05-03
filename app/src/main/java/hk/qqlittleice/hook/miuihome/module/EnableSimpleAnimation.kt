package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.ktx.setReturnConstant

class EnableSimpleAnimation {

    fun init(){
        if (OwnSP.ownSP.getBoolean("simpleAnimation", false)){
            "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant("isUseSimpleAnim", result = true)
        } else {
            "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant("isUseSimpleAnim", result = false)
        }
    }

}