package com.yuk.miuihome.module

import android.content.Context
import com.github.kyuubiran.ezxhelper.init.InitFields.moduleRes
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.yuk.miuihome.utils.OwnSP

class ModifyShowDockIconTitles {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("showDockIconTitles", false)) return
       val deviceConfigClass =  loadClass("com.miui.home.launcher.DeviceConfig")
        findMethod(deviceConfigClass) {
            name == "isHotseatsAppTitleHided"
        }.hookReturnConstant(false)
        findMethod(deviceConfigClass) {
            name == "calcHotSeatsHeight" && parameterTypes[0] == Context::class.java && parameterTypes[1] == Boolean::class.java
        }.hookAfter {
            val height = it.result as Int
            it.result = (height + 8 * moduleRes.displayMetrics.density).toInt()
        }
    }
}