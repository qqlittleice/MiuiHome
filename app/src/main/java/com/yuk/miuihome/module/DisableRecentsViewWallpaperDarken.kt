package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.setReturnConstant

class DisableRecentsViewWallpaperDarken {

    private val surfaceControlCompat = "com.android.systemui.shared.recents.system.SurfaceControlCompat".findClass()
    fun init() {

        if (OwnSP.ownSP.getBoolean("wallpaperDarken", false)){
            "com.android.systemui.shared.recents.system.TransactionCompat".setReturnConstant("show", surfaceControlCompat, result = null)
        }
    }
}