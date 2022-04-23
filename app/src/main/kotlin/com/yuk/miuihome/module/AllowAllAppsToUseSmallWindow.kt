package com.yuk.miuihome.module

import android.content.Context
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class AllowAllAppsToUseSmallWindow {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("supportSmallWindow", false)) return
        "com.miui.home.launcher.RecentsAndFSGestureUtils".hookBeforeMethod(
            "isTaskSupportSmallWindow",
            Context::class.java,
            Int::class.javaPrimitiveType
        ) {
            it.result = true
        }
        "com.miui.home.launcher.RecentsAndFSGestureUtils".hookBeforeMethod(
            "isPkgSupportSmallWindow",
            Context::class.java,
            String::class.java
        ) {
            it.result = true
        }
    }
}