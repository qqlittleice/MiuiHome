package com.yuk.miuihome.view

import android.app.AlertDialog
import android.graphics.Color
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.R
import com.yuk.miuihome.XposedInit.Companion.moduleRes
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.OwnSP.remove
import com.yuk.miuihome.utils.dip2px
import com.yuk.miuihome.utils.isNightMode

class SettingUserInputNumber(
    private val mText: String,
    private val mKey: String,
    private val minValue: Int,
    private val maxValue: Int,
    private val defValue: Int,
    private val divide: Int = 100
) {
    private val editor by lazy { ownSP.edit() }

    fun build(): AlertDialog {
        lateinit var editText: EditText
        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                addView(
                    SettingTextView.FastBuilder(
                        mText = "「${mText}」",
                        mSize = SettingTextView.text2Size,
                        mColor = "#0C84FF"
                    ).build()
                )
                addView(EditText(HomeContext.context).apply {
                    editText = this
                    inputType = EditorInfo.TYPE_CLASS_NUMBER
                    setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000"))
                })
                addView(
                    SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.Defaults) + " : $defValue")
                        .build()
                )
                addView(
                    SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.Scope) + " : $minValue ~ $maxValue")
                        .build()
                )
            })
        })
        dialogBuilder.apply {
            setPositiveButton(moduleRes.getString(R.string.Save), null)
            setNeutralButton(moduleRes.getString(R.string.Reset1)) { dialog, _ ->
                remove(mKey)
                dialog.dismiss()
            }
        }
        dialogBuilder.show().apply {
            this.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                try {
                    if (saveValue(editText.text.toString().toFloat() / divide)) {
                        LogUtil.toast("「${mText}」" + moduleRes.getString(R.string.SetSuccessfully))
                        this.dismiss()
                    }
                } catch (e: NumberFormatException) {
                    LogUtil.toast(moduleRes.getString(R.string.OutOfInput))
                }
            }
            return this
        }
    }

    private fun saveValue(value: Float): Boolean {
        if ((value < (minValue.toFloat() / divide)) or (value > (maxValue.toFloat() / divide)) or (value == -1f)) {
            LogUtil.toast(moduleRes.getString(R.string.OutOfInput))
            return false
        }
        editor.putFloat(mKey, value)
        editor.apply()
        return true
    }
}