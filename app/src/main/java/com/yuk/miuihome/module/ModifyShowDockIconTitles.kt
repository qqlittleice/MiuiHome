package com.yuk.miuihome.module

import android.content.Context
import com.yuk.miuihome.view.utils.HomeContext
import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.hookAfterMethod
import com.yuk.miuihome.view.utils.ktx.setReturnConstant

class ModifyShowDockIconTitles {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("showDockIconTitles", false)) return
        "com.miui.home.launcher.DeviceConfig".setReturnConstant("isHotseatsAppTitleHided", result = false)
        "com.miui.home.launcher.DeviceConfig".hookAfterMethod("calcHotSeatsHeight", Context::class.java, Boolean::class.java
        ) {
            val height = it.result as Int
            it.result = (height + 8 * HomeContext.context.resources.displayMetrics.density).toInt()
        }
    }
}