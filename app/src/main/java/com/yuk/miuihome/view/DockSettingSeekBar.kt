package com.yuk.miuihome.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.init.InitFields
import com.yuk.miuihome.utils.dip2px
import com.yuk.miuihome.utils.dp2px
import com.yuk.miuihome.utils.sp2px


@SuppressLint("ViewConstructor")
class SettingSeekBar(context: Context, minValue: Int = 0, maxValue: Int = 100) :
    LinearLayout(context) {
    var text: String = ""
        set(value) {
            field = value
            textView.text = value
        }
    var value: Int = 0
        set(value) {
            field = value
            seekBar.progress = value
            valueView.text = value.toString()
        }
    private val textView: TextView
    private val valueView: TextView
    private val seekBar: SeekBar


    init {
        orientation = VERTICAL
        setPadding(dip2px(5), dip2px(5), dip2px(5), dip2px(5))
        textView = TextView(context)
        valueView = TextView(context).apply {
            text = (maxValue / 2).toString()
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        }
        textView.textSize = sp2px(5f)
        seekBar = SeekBar(context).apply {
            min = minValue
            max = maxValue
            progress = valueView.text.toString().toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    value = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
        addView(textView.apply {
            setPadding(0, 0, 0, dip2px(5))
        })
        addView(seekBar)
        addView(LinearLayout(context).apply {
            orientation = HORIZONTAL
            addView(TextView(context).apply {
                text = minValue.toString()
            }, LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f))
            addView(valueView, LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f))
            addView(TextView(context).apply {
                text = maxValue.toString()
                textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END
            }, LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f))
        })
    }


    class Builder(
        private val mContext: Context = InitFields.appContext,
        private val mText: String,
        private val defaultValue: Int = 0,
        private val minVal: Int = 0,
        private val maxVal: Int = 100
    ) {
        fun build() = SettingSeekBar(mContext, minVal, maxVal).apply {
            text = mText
            value = defaultValue
        }
    }
}