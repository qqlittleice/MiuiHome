package com.yuk.miuihome.module

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP

class ModifyIconTitleFontColor {

    fun init() {
        val value = OwnSP.ownSP.getString("iconTitleFontColor", "")
        val launcherClass = loadClass("com.miui.home.launcher.Launcher")
        val shortcutInfoClass = loadClass("com.miui.home.launcher.ShortcutInfo")
        if (value == "") return
        try {
            findMethod("com.miui.home.launcher.ItemIcon") {
                name == "onFinishInflate"
            }.hookAfter {
                val mTitle = it.thisObject.getObject("mTitle") as TextView
                mTitle.setTextColor(Color.parseColor(value))
            }
            findMethod("com.miui.home.launcher.maml.MaMlWidgetView") {
                name == "onFinishInflate"
            }.hookAfter {
                val mTitle = it.thisObject.getObject("mTitle") as TextView
                mTitle.setTextColor(Color.parseColor(value))
            }
            findMethod("com.miui.home.launcher.LauncherMtzGadgetView") {
                name == "onFinishInflate"
            }.hookAfter {
                val mTitle = it.thisObject.getObject("mTitle") as TextView
                mTitle.setTextColor(Color.parseColor(value))
            }
            findMethod("com.miui.home.launcher.LauncherWidgetView") {
                name == "onFinishInflate"
            }.hookAfter {
                val mTitle = it.thisObject.getObject("mTitle") as TextView
                mTitle.setTextColor(Color.parseColor(value))
            }
            findMethod("com.miui.home.launcher.ShortcutIcon") {
                name == "fromXml" && parameterTypes[0] == Int::class.javaPrimitiveType && parameterTypes[1] == launcherClass && parameterTypes[2] == ViewGroup::class.java && parameterTypes[3] == shortcutInfoClass
            }.hookAfter {
                val buddyIconView = it.args[3].invokeMethodAuto("getBuddyIconView", it.args[2]) as View
                val mTitle = buddyIconView.getObject("mTitle") as TextView
                mTitle.setTextColor(Color.parseColor(value))
            }
            findMethod("com.miui.home.launcher.ShortcutIcon") {
                name == "createShortcutIcon" && parameterTypes[0] == Int::class.javaPrimitiveType && parameterTypes[1] == launcherClass && parameterTypes[2] == ViewGroup::class.java
            }.hookAfter {
                val buddyIcon = it.result as View
                val mTitle = buddyIcon.getObject("mTitle") as TextView
                mTitle.setTextColor(Color.parseColor(value))
            }
            findMethod("com.miui.home.launcher.common.Utilities") {
                name == "adaptTitleStyleToWallpaper" && parameterTypes[0] == Context::class.java && parameterTypes[1] == TextView::class.java && parameterTypes[2] == Int::class.javaPrimitiveType && parameterTypes[3] == Int::class.javaPrimitiveType
            }.hookAfter {
                val mTitle = it.args[1] as TextView
                if (mTitle.id == mTitle.resources.getIdentifier("icon_title", "id", Config.hostPackage))
                    mTitle.setTextColor(Color.parseColor(value))
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
    }
}