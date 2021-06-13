package com.yuk.miuihome.view

import android.app.AlertDialog
import android.graphics.Color
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.dp2px
import com.yuk.miuihome.utils.isNightMode

class SettingUserInput(private val mText: String, private val mKey: String, private val minValue: Int, private val maxValue: Int,
                       private val divide: Int = 100, private val defval:Int) {

    private val sharedPreferences = OwnSP.ownSP
    private val editor by lazy { sharedPreferences.edit() }

    fun build(): AlertDialog {
        lateinit var editText: EditText
        val dialogBuilder = SettingBaseDialog().get()
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(
                    dp2px(HomeContext.context, 10f),
                    dp2px(HomeContext.context, 10f),
                    dp2px(HomeContext.context, 10f),
                    dp2px(HomeContext.context, 5f)
                )
                addView(
                    SettingTextView.FastBuilder(
                        mText = "「${mText}」",
                        mSize = SettingTextView.text2Size,
                        mColor = "#0C84FF")
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
                        mText = "官方默认值：$defval")
                        .build()
                )
                addView(
                    SettingTextView.FastBuilder(
                        mText = "可输入范围：$minValue~$maxValue")
                        .build()
                )
                addView(
                    SettingTextView.FastBuilder(
                        mText = "输入的值会被除以$divide")
                        .build()
                )
            })
        })
        dialogBuilder.apply {
            setPositiveButton("保存", null)
            setNeutralButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
        }
        dialogBuilder.show().apply {
            this.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                try {
                    if (saveValue(editText.text.toString().toFloat() / divide)) {
                        LogUtil.toast("「${mText}」设置成功")
                        this.dismiss()
                    }
                } catch (e: NumberFormatException) {
                    LogUtil.toast("请输入允许范围内的值！")
                }
            }
            return this
        }
    }

    private fun saveValue(value: Float): Boolean {
        if ((value < (minValue.toFloat() / divide)) or (value > (maxValue.toFloat() / divide))) {
            LogUtil.toast("请输入允许范围内的值！")
            return false
        }
        editor.putFloat(mKey, value)
        editor.apply()
        return true
    }

}