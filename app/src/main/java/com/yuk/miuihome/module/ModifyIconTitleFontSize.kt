package com.yuk.miuihome.module

import android.util.TypedValue
import android.widget.TextView
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.getObjectField
import com.yuk.miuihome.utils.ktx.hookAfterMethod

class ModifyIconTitleFontSize {

    fun init() {
        val value = OwnSP.ownSP.getFloat("iconTitleFontSize", -1f)
        if (value == -1f || value == 12f) return
        "com.miui.home.launcher.ItemIcon".hookAfterMethod("onFinishInflate"
        ) {
            val mTitle = it.thisObject.getObjectField("mTitle") as TextView
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
        }
        "com.miui.home.launcher.maml.MaMlWidgetView".hookAfterMethod("onFinishInflate"
        ) {
            val mTitle = it.thisObject.getObjectField("mTitleTextView") as TextView
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
        }
        "com.miui.home.launcher.LauncherMtzGadgetView".hookAfterMethod("onFinishInflate"
        ) {
            val mTitle = it.thisObject.getObjectField("mTitleTextView") as TextView
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
        }
        "com.miui.home.launcher.LauncherWidgetView".hookAfterMethod("onFinishInflate"
        ) {
            val mTitle = it.thisObject.getObjectField("mTitleTextView") as TextView
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, value)
        }
    }
}
