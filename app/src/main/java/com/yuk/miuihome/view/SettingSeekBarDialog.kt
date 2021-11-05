package com.yuk.miuihome.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.R
import com.yuk.miuihome.XposedInit.Companion.moduleRes
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.dip2px
import com.yuk.miuihome.utils.isNightMode

@SuppressLint("SetTextI18n")
class SettingSeekBarDialog(
    private val mText: String,
    private val mKey: String,
    private val minValue: Int,
    private val maxValue: Int,
    private val divide: Int = 100,
    private val defValue: Int,
    private val canUserInput: Boolean
) {
    private val editor by lazy { ownSP.edit() }

    fun saveValue(value: Float): Boolean {
        if ((value < (minValue.toFloat() / divide)) or (value > (maxValue.toFloat() / divide))) {
            LogUtil.toast(moduleRes.getString(R.string.OutOfInput))
            return false
        }
        editor.putFloat(mKey, value)
        editor.apply()
        return true
    }

    fun build(): AlertDialog {
        val dialogBuilder = SettingBaseDialog().get()
        var tempValue: Float = ownSP.getFloat(mKey, 0f)
        lateinit var valueTextView: TextView
        lateinit var dialog: AlertDialog
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
                addView(SeekBar(HomeContext.context).apply {
                    min = minValue
                    max = maxValue
                    progress = (tempValue * divide).toInt()
                    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                            saveValue(progress.toFloat() / divide)
                            valueTextView.text = "$progress"
                            tempValue = (progress.toFloat() / divide)
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                    })
                })
                addView(LinearLayout(HomeContext.context).apply {
                    addView(TextView(HomeContext.context).apply {
                        text = "$minValue"
                        layoutParams =
                            LinearLayout.LayoutParams(150, LinearLayout.LayoutParams.MATCH_PARENT)
                        setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000"))
                    })
                    addView(TextView(HomeContext.context).apply {
                        text = "${(tempValue * divide).toInt()}"
                        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        valueTextView = this
                        layoutParams =
                            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                        setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000"))
                    })
                    addView(TextView(HomeContext.context).apply {
                        text = "$maxValue"
                        textAlignment = TextView.TEXT_ALIGNMENT_TEXT_END
                        layoutParams =
                            LinearLayout.LayoutParams(150, LinearLayout.LayoutParams.MATCH_PARENT)
                        setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000"))
                    })
                    gravity = Gravity.CENTER_VERTICAL
                })
                if (canUserInput) {
                    addView(
                        SettingTextView.FastBuilder(
                            mText = moduleRes.getString(R.string.ManualInput)
                        ) {
                            dialog.dismiss()
                            SettingUserInputNumber(
                                mText,
                                mKey,
                                minValue,
                                maxValue,
                                divide,
                                defValue
                            ).build()
                        }.build()
                    )
                }
            })
        })
        dialog = dialogBuilder.show()
        return dialog
    }
}