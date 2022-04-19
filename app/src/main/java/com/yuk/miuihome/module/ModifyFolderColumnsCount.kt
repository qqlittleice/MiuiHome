package com.yuk.miuihome.module

import android.view.ViewGroup
import android.widget.GridView
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookAfterAllMethods
import de.robv.android.xposed.XposedHelpers

class ModifyFolderColumnsCount {

    fun init() {
        val value = OwnSP.ownSP.getInt("folderColumns", -1)
        if (value == -1 || value == 3) return
        "com.miui.home.launcher.Folder".findClass().hookAfterAllMethods("bind"
        ) {
            val columns: Int = value
            val mContent = XposedHelpers.getObjectField(it.thisObject, "mContent") as GridView
            mContent.numColumns = columns
            if (OwnSP.ownSP.getBoolean("folderWidth", false) && (columns > 3)) {
                mContent.setPadding(0,0,0,0)
                val lp = mContent.layoutParams
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                mContent.layoutParams = lp
            }
            if (columns > 3) {
                val mBackgroundView = XposedHelpers.getObjectField(it.thisObject, "mBackgroundView") as ViewGroup
                mBackgroundView.setPadding(mBackgroundView.paddingLeft / 3, mBackgroundView.paddingTop, mBackgroundView.paddingRight / 3, mBackgroundView.paddingBottom)
            }
        }
    }
}