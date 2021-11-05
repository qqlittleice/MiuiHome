package com.yuk.miuihome.module


import android.view.View
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import de.robv.android.xposed.XposedHelpers

class ModifyCloseFolderOnLaunch {

    fun init() {
        if (ownSP.getBoolean("closeFolder", false)) {
            "com.miui.home.launcher.Launcher".hookAfterMethod(
                "launch",
                "com.miui.home.launcher.ShortcutInfo",
                View::class.java
            ) {
                val mHasLaunchedAppFromFolder =
                    XposedHelpers.getBooleanField(it.thisObject, "mHasLaunchedAppFromFolder")
                if (mHasLaunchedAppFromFolder) {
                    XposedHelpers.callMethod(
                        it.thisObject,
                        "closeFolder"
                    )
                }
            }
        }
    }
}
