package com.yuk.miuihome.module

import android.view.View
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import de.robv.android.xposed.XposedHelpers

//From CustoMIUIzer
class ModifyHideIconTitles {

    fun init() {
        if (OwnSP.ownSP.getBoolean("icons", false)) {
            "com.miui.home.launcher.ItemIcon".hookAfterMethod(
                "onFinishInflate"
            ) {
                val mTitleContainer: View =
                    XposedHelpers.getObjectField(it.thisObject, "mTitleContainer") as View
                mTitleContainer.visibility = View.GONE
            }
        }
    }
}
