package com.yuk.miuihome.module


import android.view.View
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.callMethod
import com.yuk.miuihome.utils.ktx.getBooleanField
import com.yuk.miuihome.utils.ktx.hookAfterMethod

class ModifyCloseFolderOnLaunch {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("closeFolder", false)) return
        "com.miui.home.launcher.Launcher".hookAfterMethod("launch", "com.miui.home.launcher.ShortcutInfo", View::class.java
        ) {
            val mHasLaunchedAppFromFolder = it.thisObject.getBooleanField("mHasLaunchedAppFromFolder")
            if (mHasLaunchedAppFromFolder) it.thisObject.callMethod("closeFolder")
        }
    }
}
