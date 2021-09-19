package com.yuk.miuihome.module

import com.yuk.miuihome.utils.ktx.setReturnConstant

class SetDeviceLevel {

    fun init() {
        "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant(
            "getDeviceLevel",
            result = 2
        )
        "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant(
            "getQualcommCpuLevel",
            String::class.java,
            result = 2
        )
        "com.mi.mibridge.DeviceLevel".setReturnConstant(
            "getDeviceLevel",
            result = 2
        )
        "com.miui.home.launcher.common.CpuLevelUtils".setReturnConstant(
            "getQualcommCpuLevel",
            String::class.java,
            result = 2
        )
        "com.miui.home.launcher.MIUIWidgetUtil".setReturnConstant(
            "isMIUIWidgetSupport",
            result = true
        )
    }
}