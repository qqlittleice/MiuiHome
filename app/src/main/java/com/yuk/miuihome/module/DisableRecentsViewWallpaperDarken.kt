package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookBeforeMethod
import com.yuk.miuihome.utils.ktx.setFloatField

class DisableRecentsViewWallpaperDarken {

    fun init() {
        if (OwnSP.ownSP.getBoolean("simpleAnimation", false)) return
        else if (!OwnSP.ownSP.getBoolean("wallpaperDarken", false)) return
        val surfaceControlCompat = "com.android.systemui.shared.recents.system.SurfaceControlCompat".findClass()
        "com.miui.home.recents.DimLayer".hookBeforeMethod("dim", Float::class.java, surfaceControlCompat, Boolean::class.java
        ) {
            it.args[0] = 0.0f
            it.thisObject.setFloatField("mCurrentAlpha", 0.0f)
        }
    }
}
