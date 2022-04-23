package com.yuk.miuihome.module

import android.view.ViewGroup
import android.widget.RelativeLayout
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.getObjectField
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import kotlin.math.roundToInt

class ModifyIconTitleTopMargin {

    fun init() {
        val titleTopMargin = OwnSP.ownSP.getInt("titleTopMargin", -1)
        if (titleTopMargin == -1) return
        "com.miui.home.launcher.ItemIcon".hookAfterMethod("onFinishInflate") {
            val mTitleContainer = it.thisObject.getObjectField("mTitleContainer") as ViewGroup
            val lp = mTitleContainer.layoutParams
            val opt = ((titleTopMargin - 11) * mTitleContainer.resources.displayMetrics.density).roundToInt()
            if (lp is RelativeLayout.LayoutParams) {
                lp.topMargin = opt
                mTitleContainer.layoutParams = lp
            } else {
                mTitleContainer.translationY = opt.toFloat()
                mTitleContainer.clipChildren = false
                mTitleContainer.clipToPadding = false
                (mTitleContainer.parent as ViewGroup).clipChildren = false
                (mTitleContainer.parent as ViewGroup).clipToPadding = false
            }
        }
    }
}