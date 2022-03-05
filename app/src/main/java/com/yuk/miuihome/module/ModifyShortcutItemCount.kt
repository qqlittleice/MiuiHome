package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.callMethod
import com.yuk.miuihome.utils.ktx.hookAfterMethod

class ModifyShortcutItemCount {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("unlockShortcutCount", false)) return
        "com.miui.home.launcher.shortcuts.AppShortcutMenu".hookAfterMethod("getMaxCountInCurrentOrientation") {
            it.result = 10
        }
        "com.miui.home.launcher.shortcuts.AppShortcutMenu".hookAfterMethod("getMaxShortcutItemCount") {
            it.result = 99
        }
        "com.miui.home.launcher.shortcuts.AppShortcutMenu".hookAfterMethod("getMaxVisualHeight") {
            it.result = it.thisObject.callMethod("getItemHeight")
        }
    }
}