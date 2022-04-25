package com.yuk.miuihome.module

import android.content.Context
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuihome.utils.OwnSP

class AllowAllAppsToUseSmallWindow {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("supportSmallWindow", false)) return
        findMethod("com.miui.home.launcher.RecentsAndFSGestureUtils") {
            name == "isTaskSupportSmallWindow" && parameterTypes[0] == Context::class.java && parameterTypes[1] == Int::class.javaPrimitiveType
        }.hookReturnConstant(true)
        findMethod("com.miui.home.launcher.RecentsAndFSGestureUtils") {
            name == "isPkgSupportSmallWindow" && parameterTypes[0] == Context::class.java && parameterTypes[1] == String::class.java
        }.hookReturnConstant(true)
    }
}