package com.yuk.miuihome.module

import android.view.ViewGroup
import android.widget.RelativeLayout
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObject
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.yuk.miuihome.utils.OwnSP
import kotlin.math.roundToInt

class ModifyIconTitleTopMargin {

    fun init() {
        val titleTopMargin = OwnSP.ownSP.getInt("titleTopMargin", -1)
        if (titleTopMargin == -1) return
        findMethod("com.miui.home.launcher.ItemIcon") {
            name == "onFinishInflate"
        }.hookAfter {
            val mTitleContainer = it.thisObject.getObject("mTitleContainer") as ViewGroup
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