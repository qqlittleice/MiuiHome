package com.yuk.miuihome.module

import android.view.Window
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class AlwaysBlurWallpaper {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("alwaysBlur", false) || OwnSP.ownSP.getBoolean("simpleAnimation", false)) return
        val value = OwnSP.ownSP.getFloat("blurRadius", 1f)
        "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("fastBlur", Float::class.java, Window::class.java, Boolean::class.java, Long::class.java
        ) {
            it.args[0] = value
            it.args[2] = true
        }
    }
}