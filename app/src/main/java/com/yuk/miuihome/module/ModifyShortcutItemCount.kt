package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.callMethod
import com.yuk.miuihome.utils.ktx.hookAfterMethod

class ModifyShortcutItemCount {

    fun init() {
        val value = OwnSP.ownSP.getInt("shortcutCount", 6)
        if (!OwnSP.ownSP.getBoolean("unlockShortcutCount", false)) return
        "com.miui.home.launcher.shortcuts.AppShortcutMenu".hookAfterMethod("getMaxCountInCurrentOrientation") {
            it.result = value
        }
        "com.miui.home.launcher.shortcuts.AppShortcutMenu".hookAfterMethod("getMaxShortcutItemCount") {
            it.result = value
        }
        "com.miui.home.launcher.shortcuts.AppShortcutMenu".hookAfterMethod("getMaxVisualHeight") {
            it.result = it.thisObject.callMethod("getItemHeight")
        }
    }
}