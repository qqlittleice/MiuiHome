package com.yuk.miuihome.module

import de.robv.android.xposed.XposedHelpers
import android.widget.GridView
import android.view.ViewGroup
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.hookAfterMethod

class ModifyFolderColumnsCount {

    fun init() {
        val value = ownSP.getFloat("folderColumns", -1f)
        if (value == -1f || value == 3f) return
        "com.miui.home.launcher.Folder".hookAfterMethod(
            "onFinishInflate"
        ) {
            val columns: Int = value.toInt()
            val mContent = XposedHelpers.getObjectField(it.thisObject, "mContent") as GridView
            mContent.numColumns = columns
            if (ownSP.getBoolean("folderWidth", false) && (columns > 3)) {
                val lp = mContent.layoutParams
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                mContent.layoutParams = lp
            }
            if (columns > 3) {
                val mBackgroundView =
                    XposedHelpers.getObjectField(it.thisObject, "mBackgroundView") as ViewGroup
                mBackgroundView.setPadding(
                    mBackgroundView.paddingLeft / 3,
                    mBackgroundView.paddingTop,
                    mBackgroundView.paddingRight / 3,
                    mBackgroundView.paddingBottom
                )
            }
        }
    }
}