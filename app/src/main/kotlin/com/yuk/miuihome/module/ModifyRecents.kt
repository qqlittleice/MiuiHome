package com.yuk.miuihome.module

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*

class ModifyRecents {

    fun init() {
        val recentsContainerClass = "com.miui.home.recents.views.RecentsContainer".findClass()
        val taskViewHeaderClass = "com.miui.home.recents.views.TaskViewHeader".findClass()
        val recentTextSize = OwnSP.ownSP.getFloat("recentTextSize", -1f)
        val emptyViewText: String = OwnSP.ownSP.getString("recentText", "").toString()
        val appCardBgColor = OwnSP.ownSP.getString("appCardBgColor", "")
        if (OwnSP.ownSP.getBoolean("smallWindow", false)) {
            recentsContainerClass.hookAfterMethod(
                "onFinishInflate"
            ) {
                val mTitle = it.thisObject.getObjectField("mTxtSmallWindow") as TextView
                mTitle.visibility = View.GONE
            }
        }
        if (OwnSP.ownSP.getBoolean("cleanUp", false)) {
            recentsContainerClass.hookAfterMethod(
                "onFinishInflate"
            ) {
                val mView = it.thisObject.getObjectField("mClearAnimView") as View
                mView.visibility = View.GONE
            }
        }
        if (recentTextSize != -1f) {
            taskViewHeaderClass.hookAfterMethod(
                "onFinishInflate"
            ) {
                val mTitle = it.thisObject.getObjectField("mTitleView") as TextView
                mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, recentTextSize)
            }
        }
        if (OwnSP.ownSP.getBoolean("recentIcon", false)) {
            taskViewHeaderClass.hookAfterMethod(
                "onFinishInflate"
            ) {
                val mImage = it.thisObject.getObjectField("mIconView") as ImageView
                mImage.visibility = View.GONE
            }
        }
        if (emptyViewText != "") {
            "com.miui.home.recents.views.RecentsView".hookAfterMethod(
                "showEmptyView", Int::class.javaPrimitiveType
            ) {
                (it.thisObject.getObjectField("mEmptyView") as TextView).apply {
                    this.text = emptyViewText
                }
            }
        }
        if (appCardBgColor != "") {
            "com.miui.home.recents.views.TaskViewThumbnail".findClass().hookAfterAllConstructors {
                it.thisObject.setIntField("mBgColorForSmallWindow", Color.parseColor(appCardBgColor))
            }
        }
    }
}