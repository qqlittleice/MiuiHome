package com.yuk.miuihome.module

import com.yuk.miuihome.utils.ktx.setReturnConstant

class SetDeviceLevel {

    fun init() {

        "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant("getDeviceLevel", result = 2)
    }

}