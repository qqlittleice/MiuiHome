package com.yuk.miuihome.module

import de.robv.android.xposed.XposedHelpers
import android.widget.GridView
import android.view.ViewGroup
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import de.robv.android.xposed.XposedBridge

class ModifyFolderColumnsCount {

    fun init() {

        "com.miui.home.launcher.Folder".hookAfterMethod(
            "onFinishInflate"
        ) {
            val columns: Int = OwnSP.ownSP.getInt("folderColumns", 1)
            XposedBridge.log("输出：$columns")
            val mContent =
                XposedHelpers.getObjectField(it.thisObject, "mContent") as GridView
            mContent.numColumns = columns
            if (OwnSP.ownSP.getBoolean("folderWidth", false)) {
                val lp = mContent.layoutParams
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                mContent.layoutParams = lp
            }
            if (columns > 3) {
                val mBackgroundView = XposedHelpers.getObjectField(
                    it.thisObject,
                    "mBackgroundView"
                ) as ViewGroup
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