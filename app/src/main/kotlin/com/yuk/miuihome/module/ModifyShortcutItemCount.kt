package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.OwnSP

class ModifyShortcutItemCount {

    fun init() {
        val value = OwnSP.ownSP.getInt("shortcutCount", 6)
        if (!OwnSP.ownSP.getBoolean("unlockShortcutCount", false)) return
        val appShortcutMenuClass = loadClass("com.miui.home.launcher.shortcuts.AppShortcutMenu")
        findMethod(appShortcutMenuClass) {
            name == "getMaxCountInCurrentOrientation"
        }.hookReturnConstant(value)
        findMethod(appShortcutMenuClass) {
            name == "getMaxShortcutItemCount"
        }.hookReturnConstant(value)
        findMethod(appShortcutMenuClass) {
            name == "getMaxVisualHeight"
        }.hookAfter {
            it.thisObject.invokeMethodAuto("getItemHeight")
        }
    }
}