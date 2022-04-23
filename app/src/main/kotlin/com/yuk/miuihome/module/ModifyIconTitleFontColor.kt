package com.yuk.miuihome.module

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.Log
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.callMethod
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.getObjectField
import com.yuk.miuihome.utils.ktx.hookAfterMethod

class ModifyIconTitleFontColor {

    fun init() {
        val value = OwnSP.ownSP.getString("iconTitleFontColor", "")
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
        val shortcutInfoClass = "com.miui.home.launcher.ShortcutInfo".findClass()
        if (value == "") return
        try {
            "com.miui.home.launcher.ItemIcon".hookAfterMethod("onFinishInflate"
            ) {
                val mTitle = it.thisObject.getObjectField("mTitle") as TextView
                mTitle.setTextColor(Color.parseColor(value))
            }
            "com.miui.home.launcher.maml.MaMlWidgetView".hookAfterMethod("onFinishInflate"
            ) {
                val mTitle = it.thisObject.getObjectField("mTitleTextView") as TextView
                mTitle.setTextColor(Color.parseColor(value))
            }
            "com.miui.home.launcher.LauncherMtzGadgetView".hookAfterMethod("onFinishInflate"
            ) {
                val mTitle = it.thisObject.getObjectField("mTitleTextView") as TextView
                mTitle.setTextColor(Color.parseColor(value))
            }
            "com.miui.home.launcher.LauncherWidgetView".hookAfterMethod("onFinishInflate"
            ) {
                val mTitle = it.thisObject.getObjectField("mTitleTextView") as TextView
                mTitle.setTextColor(Color.parseColor(value))
            }
            "com.miui.home.launcher.ShortcutIcon".hookAfterMethod("fromXml", Int::class.javaPrimitiveType, launcherClass, ViewGroup::class.java, shortcutInfoClass
            ) {
                val buddyIconView = it.args[3].callMethod("getBuddyIconView", it.args[2]) as View
                val mTitle = buddyIconView.getObjectField("mTitle") as TextView
                mTitle.setTextColor(Color.parseColor(value))
            }
            "com.miui.home.launcher.ShortcutIcon".hookAfterMethod("createShortcutIcon", Int::class.javaPrimitiveType, launcherClass, ViewGroup::class.java
            ) {
                val buddyIcon = it.result as View
                val mTitle = buddyIcon.getObjectField("mTitle") as TextView
                mTitle.setTextColor(Color.parseColor(value))
            }
            "com.miui.home.launcher.common.Utilities".hookAfterMethod("adaptTitleStyleToWallpaper", Context::class.java, TextView::class.java, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType
            ) {
                val mTitle = it.args[1] as TextView
                if (mTitle.id == mTitle.resources.getIdentifier("icon_title", "id", Config.hostPackage))
                    mTitle.setTextColor(Color.parseColor(value))
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
    }
}