package com.yuk.miuihome.module

import android.content.Context
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class AllowAllAppsToUseSmallWindow {

    fun init() {
        if (ownSP.getBoolean("supportSmallWindow", false)) {
            "com.miui.home.launcher.RecentsAndFSGestureUtils".setReturnConstant(
                "isTaskSupportSmallWindow",
                Context::class.java,
                Int::class.javaPrimitiveType,
                result = true
            )

            "com.miui.home.launcher.RecentsAndFSGestureUtils".setReturnConstant(
                "isPkgSupportSmallWindow",
                Context::class.java,
                String::class.java,
                result = true
            )
        }
    }
}