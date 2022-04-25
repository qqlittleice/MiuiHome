package com.yuk.miuihome.module

import android.view.Window
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.yuk.miuihome.utils.OwnSP

class AlwaysBlurWallpaper {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("alwaysBlur", false) || OwnSP.ownSP.getBoolean("simpleAnimation", false)) return
        val value = OwnSP.ownSP.getFloat("blurRadius", 1f)
        findMethod("com.miui.home.launcher.common.BlurUtils") {
            name == "fastBlur" && parameterTypes[0] == Float::class.java && parameterTypes[1] == Window::class.java && parameterTypes[2] == Boolean::class.java && parameterTypes[3] == Long::class.java
        }.hookBefore {
            it.args[0] = value
            it.args[2] = true
        }
    }
}