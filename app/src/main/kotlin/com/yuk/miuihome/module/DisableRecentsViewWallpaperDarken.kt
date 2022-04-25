package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.putObject
import com.yuk.miuihome.utils.OwnSP

class DisableRecentsViewWallpaperDarken {

    fun init() {
        if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) return
        else if (!OwnSP.ownSP.getBoolean("wallpaperDarken", false)) return
        findMethod("com.miui.home.recents.DimLayer") {
            name == "dim" && parameterTypes[0] == Float::class.java && parameterTypes[2] == Boolean::class.java && parameterCount == 3
        }.hookBefore {
            it.args[0] = 0.0f
            it.thisObject.putObject("mCurrentAlpha", 0.0f)
        }
    }
}
