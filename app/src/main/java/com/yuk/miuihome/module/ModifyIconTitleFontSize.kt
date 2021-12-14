package com.yuk.miuihome.module

import android.util.TypedValue
import android.widget.TextView
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.callMethod
import com.yuk.miuihome.utils.ktx.getObjectField
import com.yuk.miuihome.utils.ktx.hookAfterMethod


class ModifyIconTitleFontSize {

    fun init() {
        val value = OwnSP.ownSP.getFloat("iconTitleFontSize", -1f)
        if (value == -1f || value == 12f) return
        "com.miui.home.launcher.ItemIcon".hookAfterMethod("onFinishInflate") {
            val mTitle = it.thisObject.getObjectField("mTitle") as TextView
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
        }
        "com.miui.home.launcher.ShortcutIcon".hookAfterMethod(
            "fromXml",
        ) {
            val mIcon = it.args[3].callMethod("getBuddyIconView", it.args[2]) ?: return@hookAfterMethod
            val mTitle = mIcon.getObjectField("mTitle") as TextView
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
        }
        "com.miui.home.launcher.ShortcutIcon".hookAfterMethod("createShortcutIcon") {
            val mIcon = it.result ?: return@hookAfterMethod
            val mTitle = mIcon.getObjectField("mTitle") as TextView
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
        }
        "com.miui.home.launcher.common.Utilities".hookAfterMethod(
            "adaptTitleStyleToWallpaper"
        ) {
            val mTitle = it.args[1] as TextView
            if (mTitle.id == mTitle.resources.getIdentifier("icon_title", "id", Config.hookPackage)) mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
        }
    }
}
