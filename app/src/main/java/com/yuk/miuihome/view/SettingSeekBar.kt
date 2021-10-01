package com.yuk.miuihome.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.dp2px
import com.yuk.miuihome.utils.isNightMode

@SuppressLint("ViewConstructor")
class SettingSeekBar(
    context: Context,
    private val mKey: String,
    private val minValue: Int = 0,
    private val maxValue: Int = 100
) :
    LinearLayout(context) {
    private var mContext: Context? = null

    var text: String = ""
        set(value) {
            field = value
            textView.text = value
        }

    private val seekBar: SeekBar
    private val textView: TextView
    private var sharedPreferences = OwnSP.ownSP
    private val editor by lazy { sharedPreferences.edit() }
    private val myRes by lazy { HomeContext.resInstance.moduleRes.resources }
    lateinit var valueTextView: TextView
    var tempValue: Float = sharedPreferences.getFloat(mKey, 0f)
    private val divide: Int = 10
    private val unit: String = " f"

    init {
        orientation = VERTICAL
        setPadding(
            dp2px(getContext(), 10f),
            dp2px(getContext(), 5f),
            dp2px(getContext(), 10f),
            dp2px(getContext(), 5f)
        )
        textView = TextView(context)
        textView.textSize = SettingTextView.textSize
        seekBar = SeekBar(HomeContext.context).apply {
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
        }
        addView(textView.apply {
            setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000"))
            setPadding(0, 0, 0, dp2px(HomeContext.context, 5f))
        })
        addView(seekBar)
        addView(LinearLayout(HomeContext.context).apply {
            addView(TextView(HomeContext.context).apply {
                text = "${(minValue / divide.toFloat())}$unit"
                layoutParams =
                    LayoutParams(110, LayoutParams.MATCH_PARENT)
                setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000"))
            })
            addView(TextView(HomeContext.context).apply {
                text = "$tempValue$unit"
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                valueTextView = this
                layoutParams =
                    LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
                setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000"))
            })
            addView(TextView(HomeContext.context).apply {
                text = "${(maxValue / divide.toFloat())}$unit"
                textAlignment = TextView.TEXT_ALIGNMENT_TEXT_END
                layoutParams =
                    LayoutParams(110, LayoutParams.MATCH_PARENT)
                setTextColor(Color.parseColor(if (isNightMode(context)) "#ffffff" else "#000000"))
            })
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            gravity = Gravity.CENTER_VERTICAL
            (this.layoutParams as LayoutParams).apply {
                topMargin = dp2px(HomeContext.context, 5f)
            }
        })
    }


    class FastBuilder(
        private val mContext: Context = HomeContext.context,
        private val mText: String,
        private val mKey: String,
        private val defValue: Float,
        private val minValue: Int = 0,
        private val maxValue: Int = 100
    ) {
        fun build() = SettingSeekBar(mContext, mKey, minValue, maxValue).apply {
            text = mText
            mKey
            defValue
            minValue
            maxValue
        }
    }

    fun saveValue(value: Float): Boolean {
        if ((value < (minValue.toFloat() / divide)) or (value > (maxValue.toFloat() / divide))) {
            LogUtil.toast(myRes.getString(R.string.OutOfInput))
            return false
        }
        editor.putFloat(mKey, value)
        editor.apply()
        return true
    }
}