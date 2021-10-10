package com.yuk.miuihome.view

import android.app.AlertDialog
import android.graphics.Color
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.*

class SettingUserInputNumber(
    private val mText: String,
    private val mKey: String,
    private val minValue: Int,
    private val maxValue: Int,
    private val divide: Int = 100,
    private val defValue: Int
) {

    private val sharedPreferences = OwnSP.ownSP
    private val editor by lazy { sharedPreferences.edit() }
    private val myRes by lazy { HomeContext.resInstance.moduleRes.resources }

    fun build(): AlertDialog {
        lateinit var editText: EditText
        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(
                    dip2px(10),
                    dip2px(6),
                    dip2px(10),
                    dip2px(6)
                )
                addView(
                    SettingTextView.FastBuilder(
                        mText = "「${mText}」",
                        mSize = SettingTextView.text2Size,
                        mColor = "#0C84FF"
                    )
                        .build()
                )
                addView(
                    EditText(HomeContext.context).apply {
                        editText = this
                        inputType = EditorInfo.TYPE_CLASS_NUMBER
                        setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000"))
                    })
                addView(
                    SettingTextView.FastBuilder(
                        mText = myRes.getString(R.string.Defaults) + " : $defValue"
                    )
                        .build()
                )
                addView(
                    SettingTextView.FastBuilder(
                        mText = myRes.getString(R.string.Scope) + " : $minValue ~ $maxValue"
                    )
                        .build()
                )
            })
        })
        dialogBuilder.apply {
            setPositiveButton(myRes.getString(R.string.Save), null)
            setNeutralButton(myRes.getString(R.string.Reset1)) { dialog, _ ->
                OwnSP.remove(mKey)
                dialog.dismiss()
            }
        }
        dialogBuilder.show().apply {
            this.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                try {
                    if (saveValue(editText.text.toString().toFloat() / divide)) {
                        LogUtil.toast("「${mText}」" + myRes.getString(R.string.SetSuccessfully))
                        this.dismiss()
                    }
                } catch (e: NumberFormatException) {
                    LogUtil.toast(myRes.getString(R.string.OutOfInput))
                }
            }
            return this
        }
    }

    private fun saveValue(value: Float): Boolean {
        if ((value < (minValue.toFloat() / divide)) or (value > (maxValue.toFloat() / divide)) or (value == -1f)) {
            LogUtil.toast(myRes.getString(R.string.OutOfInput))
            return false
        }
        editor.putFloat(mKey, value)
        editor.apply()
        return true
    }
}