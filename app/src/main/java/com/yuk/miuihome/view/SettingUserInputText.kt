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
import com.yuk.miuihome.utils.OwnSP.ownSP

class SettingUserInputText(
    private val mText: String,
    private val mKey: String
) {

    private val editor by lazy { ownSP.edit() }
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
                        inputType = EditorInfo.TYPE_CLASS_TEXT
                        setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000"))
                    })
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
                    if (saveValue(editText.text.toString())) {
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


    private fun saveValue(value: String): Boolean {
        editor.putString(mKey, value)
        editor.apply()
        return true
    }
}