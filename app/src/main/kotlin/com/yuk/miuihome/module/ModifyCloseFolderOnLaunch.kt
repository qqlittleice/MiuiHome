package com.yuk.miuihome.module

import android.view.View
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.OwnSP

class ModifyCloseFolderOnLaunch {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("closeFolder", false)) return
        findMethod("com.miui.home.launcher.Launcher") {
            name == "launch" && parameterTypes[0] == loadClass("com.miui.home.launcher.ShortcutInfo") && parameterTypes[1] == View::class.java
        }.hookAfter {
            val mHasLaunchedAppFromFolder = it.thisObject.getObjectAs<Boolean>("mHasLaunchedAppFromFolder")
            if (mHasLaunchedAppFromFolder) it.thisObject.invokeMethodAuto("closeFolder")
        }
    }
}
