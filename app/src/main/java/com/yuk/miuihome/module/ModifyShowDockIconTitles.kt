package com.yuk.miuihome.module

import android.content.Context
import com.github.kyuubiran.ezxhelper.init.InitFields.moduleRes
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class ModifyShowDockIconTitles {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("showDockIconTitles", false)) return
        "com.miui.home.launcher.DeviceConfig".hookBeforeMethod("isHotseatsAppTitleHided") {
            it.result = false
        }
        "com.miui.home.launcher.DeviceConfig".hookAfterMethod(
            "calcHotSeatsHeight", Context::class.java, Boolean::class.java
        ) {
            val height = it.result as Int
            it.result = (height + 8 * moduleRes.displayMetrics.density).toInt()
        }
    }
}