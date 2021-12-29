package com.yuk.miuihome.module

import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.ktx.setReturnConstant

class SetDeviceLevel : BaseClassAndMethodCheck {

    fun init() {
        try {
            "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant("getDeviceLevel", result = 2)
            "com.miui.home.launcher.common.CpuLevelUtils".setReturnConstant("getQualcommCpuLevel", String::class.java, result = 2)
            "com.miui.home.launcher.DeviceConfig".setReturnConstant("isSupportCompleteAnimation", result = true)
            "com.miui.home.launcher.common.DeviceLevelUtils".setReturnConstant("isLowLevelOrLiteDevice", result = false)
            "com.miui.home.launcher.DeviceConfig".setReturnConstant("isDefaultIcon", result = true)
            "com.miui.home.launcher.DeviceConfig".setReturnConstant("isMiuiLiteVersion", result = false)
            runWithChecked {
                "com.miui.home.launcher.util.noword.NoWordSettingHelperKt".setReturnConstant("isNoWordAvailable", result = true)
            }
        } catch (e: Throwable) {
            LogUtil.e(e)
        }
    }

    override fun classAndMethodList(): ArrayList<String> = arrayListOf(
        "com.miui.home.launcher.util.noword.NoWordSettingHelperKt", "isNoWordAvailable"
    )
}