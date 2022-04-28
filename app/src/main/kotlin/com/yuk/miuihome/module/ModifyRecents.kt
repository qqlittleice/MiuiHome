package com.yuk.miuihome.module

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.OwnSP

class ModifyRecents {

    fun init() {
        val recentsContainerClass = loadClass("com.miui.home.recents.views.RecentsContainer")
        val taskViewHeaderClass = loadClass("com.miui.home.recents.views.TaskViewHeader")
        val recentTextSize = OwnSP.ownSP.getFloat("recentTextSize", -1f)
        val emptyViewText: String = OwnSP.ownSP.getString("recentText", "").toString()
        val appCardBgColor = OwnSP.ownSP.getString("appCardBgColor", "")
        if (OwnSP.ownSP.getBoolean("smallWindow", false)) {
            findMethod(recentsContainerClass) {
                name == "onFinishInflate"
            }.hookAfter {
                val mTitle = it.thisObject.getObject("mTxtSmallWindow") as TextView
                mTitle.visibility = View.GONE
            }
        }
        if (OwnSP.ownSP.getBoolean("cleanUp", false)) {
            findMethod(recentsContainerClass) {
                name == "onFinishInflate"
            }.hookAfter {
                val mTitle = it.thisObject.getObject("mClearAnimView") as TextView
                mTitle.visibility = View.GONE
            }
        }
        if (recentTextSize != -1f) {
            findMethod(taskViewHeaderClass) {
                name == "onFinishInflate"
            }.hookAfter {
                val mTitle = it.thisObject.getObject("mTitleView") as TextView
                mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, recentTextSize)
            }
        }
        if (OwnSP.ownSP.getBoolean("recentIcon", false)) {
            findMethod(taskViewHeaderClass) {
                name == "onFinishInflate"
            }.hookAfter {
                val mImage = it.thisObject.getObject("mIconView") as ImageView
                mImage.visibility = View.GONE
            }
        }
        if (emptyViewText != "") {
            findMethod("com.miui.home.recents.views.RecentsView") {
                name == "showEmptyView" && parameterTypes[0] == Int::class.javaPrimitiveType
            }.hookAfter {
                (it.thisObject.getObject("mEmptyView") as TextView).apply {
                    this.text = emptyViewText
                }
            }
        }
        if (appCardBgColor != "") {
            hookAllConstructorAfter("com.miui.home.recents.views.TaskViewThumbnail") {
                it.thisObject.putObject("mBgColorForSmallWindow", Color.parseColor(appCardBgColor))
            }
        }
    }
}