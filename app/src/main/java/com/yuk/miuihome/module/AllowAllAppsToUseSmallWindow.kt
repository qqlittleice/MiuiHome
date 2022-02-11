package com.yuk.miuihome.module

import android.content.Context
import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.setReturnConstant

class AllowAllAppsToUseSmallWindow {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("supportSmallWindow", false)) return
        "com.miui.home.launcher.RecentsAndFSGestureUtils".setReturnConstant("isTaskSupportSmallWindow", Context::class.java, Int::class.javaPrimitiveType, result = true)
        "com.miui.home.launcher.RecentsAndFSGestureUtils".setReturnConstant("isPkgSupportSmallWindow", Context::class.java, String::class.java, result = true)
    }
}