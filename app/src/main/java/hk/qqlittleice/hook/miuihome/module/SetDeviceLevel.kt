package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.utils.ktx.setReturnConstant

class SetDeviceLevel {

    fun init() {
        "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant("getDeviceLevel", result = 2)
    }

}