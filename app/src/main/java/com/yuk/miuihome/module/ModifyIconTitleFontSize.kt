package com.yuk.miuihome.module

import android.util.TypedValue
import android.widget.TextView
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import de.robv.android.xposed.XposedHelpers


class ModifyIconTitleFontSize {

    fun init() {
        val value = ownSP.getFloat("iconTitleFontSize", -1f)
        if (value == -1f || value == 12f) return
        "com.miui.home.launcher.ItemIcon".hookAfterMethod("onFinishInflate") {
            val mTitle = XposedHelpers.getObjectField(it.thisObject, "mTitle") as TextView
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
        }
        "com.miui.home.launcher.ShortcutIcon".hookAfterMethod(
            "fromXml",
        ) {
            val mIcon = XposedHelpers.callMethod(it.args[3], "getBuddyIconView", it.args[2])
                ?: return@hookAfterMethod
            val mTitle = XposedHelpers.getObjectField(mIcon, "mTitle") as TextView
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
        }
        "com.miui.home.launcher.ShortcutIcon".hookAfterMethod("createShortcutIcon") {
            val mIcon = it.result ?: return@hookAfterMethod
            val mTitle = XposedHelpers.getObjectField(mIcon, "mTitle") as TextView
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
        }
        "com.miui.home.launcher.common.Utilities".hookAfterMethod(
            "adaptTitleStyleToWallpaper"
        ) {
            val mTitle = it.args[1] as TextView
            if (mTitle.id == mTitle.resources.getIdentifier(
                    "icon_title",
                    "id",
                    "com.miui.home"
                )
            ) mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
        }
    }
}
