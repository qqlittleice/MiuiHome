package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant

class SetDeviceLevel : BaseClassAndMethodCheck {

    fun init() {
        try {
            findMethod("com.miui.home.launcher.common.DeviceLevelUtils") {
                name == "getDeviceLevel"
            }.hookReturnConstant(2)
            findMethod("com.miui.home.launcher.common.CpuLevelUtils") {
                name == "getQualcommCpuLevel" && parameterTypes[0] == String::class.java
            }.hookReturnConstant(2)
            findMethod("com.miui.home.launcher.DeviceConfig") {
                name == "isSupportCompleteAnimation"
            }.hookReturnConstant(true)
            findMethod("com.miui.home.launcher.common.DeviceLevelUtils") {
                name == "isLowLevelOrLiteDevice"
            }.hookReturnConstant(false)
            findMethod("com.miui.home.launcher.DeviceConfig") {
                name == "isMiuiLiteVersion"
            }.hookReturnConstant(false)
            //findMethod("com.miui.home.launcher.DeviceConfig") {
                //name == "isDefaultIcon"
            //}.hookReturnConstant(true)
            runWithChecked {
                findMethod("com.miui.home.launcher.util.noword.NoWordSettingHelperKt") {
                    name == "isNoWordAvailable"
                }.hookReturnConstant(true)
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
    }

    override fun classAndMethodList(): ArrayList<String> = arrayListOf("com.miui.home.launcher.util.noword.NoWordSettingHelperKt", "isNoWordAvailable")
}