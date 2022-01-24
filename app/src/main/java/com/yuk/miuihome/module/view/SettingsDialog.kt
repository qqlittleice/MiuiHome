package com.yuk.miuihome.module.view

import android.app.AlertDialog
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.yuk.miuihome.R
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.module.ModifyBlurLevel
import com.yuk.miuihome.module.view.data.DataHelper
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.dip2px
import com.yuk.miuihome.view.SettingTextView

class SettingsDialog {

    fun showModifyBlurLevel() {
        val dialogBuilder = SettingsBaseDialog().get()
        lateinit var dialog: AlertDialog
        lateinit var onClick: View
        dialogBuilder.setView(ScrollView(DataHelper.currentActivity).apply {
            overScrollMode = 2
            addView(LinearLayout(DataHelper.currentActivity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                addView(SettingTextView.FastBuilder(mText = "「" + XposedInit.moduleRes.getString(R.string.TaskViewBlurLevel) + "」", mSize = SettingTextView.text2Size, mColor = "#0C84FF").build())
                addView(SettingTextView.FastBuilder(mText = XposedInit.moduleRes.getString(R.string.CompleteBlur)) {
                    OwnSP.set("blurLevel", 2f)
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = XposedInit.moduleRes.getString(R.string.TestBlur)) {
                    OwnSP.set("blurLevel", 3f)
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = XposedInit.moduleRes.getString(R.string.BasicBlur), show = ModifyBlurLevel.checked) {
                    OwnSP.set("blurLevel", 4f)
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = XposedInit.moduleRes.getString(R.string.SimpleBlur)) {
                    OwnSP.set("blurLevel", 1f)
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = XposedInit.moduleRes.getString(R.string.NoneBlur)) {
                    OwnSP.set("blurLevel", 0f)
                    onClick = it
                    dialog.dismiss()
                }.build())
            })
        })
        dialog = dialogBuilder.show().apply {
            setOnDismissListener {
                try {
                    LogUtil.toast(XposedInit.moduleRes.getString(R.string.TaskViewBlurSetTo) + " : ${(onClick as TextView).text}")
                } catch (ignore: Exception) {
                }
            }
        }
    }
}