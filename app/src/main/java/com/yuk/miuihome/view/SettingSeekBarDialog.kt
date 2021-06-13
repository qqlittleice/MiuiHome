package com.yuk.miuihome.view

import android.app.AlertDialog
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.dp2px
import com.yuk.miuihome.utils.isNightMode

class SettingSeekBarDialog(
    private val mText: String,
    private val mKey: String,
    private val minValue: Int,
    private val maxValue: Int,
    private val divide: Int = 100,
    private val canUserInput: Boolean,
    private val unit: String = " f",
    private val defval: Int
) {

    private val sharedPreferences = OwnSP.ownSP
    private val editor by lazy { sharedPreferences.edit() }

    fun saveValue(value: Float): Boolean {
        if ((value < (minValue.toFloat() / divide)) or (value > (maxValue.toFloat() / divide))) {
            LogUtil.toast("请输入允许范围内的值！")
            return false
        }
        editor.putFloat(mKey, value)
        editor.apply()
        return true
    }

    fun build(): AlertDialog {
        val dialogBuilder = SettingBaseDialog().get()
        var tempValue: Float = sharedPreferences.getFloat(mKey, 0f)
        lateinit var valueTextView: TextView
        lateinit var dialog: AlertDialog
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
                        mText ="「${mText}」",
                        mSize = SettingTextView.text2Size,
                        mColor = "#0C84FF")
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
                            valueTextView.text = "$tempValue$unit"
                            tempValue = (progress.toFloat() / divide)
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                    })
                })
                addView(LinearLayout(HomeContext.context).apply {
                    addView(TextView(HomeContext.context).apply {
                        text = "${(minValue / divide.toFloat())}$unit"
                        layoutParams =
                            LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.MATCH_PARENT)
                        setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000"))
                    })
                    addView(TextView(HomeContext.context).apply {
                        text = "$tempValue$unit"
                        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        valueTextView = this
                        layoutParams =
                            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                        setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000"))
                    })
                    addView(TextView(HomeContext.context).apply {
                        text = "${(maxValue / divide.toFloat())}$unit"
                        textAlignment = TextView.TEXT_ALIGNMENT_TEXT_END
                        layoutParams =
                            LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.MATCH_PARENT)
                        setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000"))
                    })
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    gravity = Gravity.CENTER_VERTICAL
                    (this.layoutParams as LinearLayout.LayoutParams).apply {
                        topMargin = dp2px(HomeContext.context, 5f)
                    }
                })
                if (canUserInput) {
                    addView(
                        SettingTextView.FastBuilder(
                            mText = "手动输入"
                        ) {
                            dialog.dismiss()
                            SettingUserInput(
                                mText,
                                mKey,
                                minValue,
                                maxValue,
                                divide,
                                defval
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
