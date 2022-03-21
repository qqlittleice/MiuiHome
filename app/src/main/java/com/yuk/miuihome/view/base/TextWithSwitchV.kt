package com.yuk.miuihome.view.base

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.view.CustomSwitch
import com.yuk.miuihome.view.data.LayoutPair

class TextWithSwitchV(
    private val textWithSummaryV: TextWithSummaryV,
    private val key: String,
    private var customOnCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null
) : BaseView() {

    override fun getType(): BaseView = this

    @SuppressLint("ClickableViewAccessibility")
    override fun create(context: Context): View {
        val switchV = CustomSwitch(context).also {
            it.background = null
            it.setThumbResource(R.drawable.switch_thumb)
            it.setTrackResource(R.drawable.switch_track)
            it.isChecked = OwnSP.ownSP.getBoolean(key, false)
            it.setOnCheckedChangeListener { compoundButton, b ->
                customOnCheckedChangeListener?.onCheckedChanged(compoundButton, b)
                OwnSP.ownSP.edit().run {
                    putBoolean(key, b)
                    apply()
                }
            }
        }
        return LinearContainerV(
            LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(textWithSummaryV.create(context).also { it.setPadding(0, dp2px(16f), 0, dp2px(16f)) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).also { it.gravity = Gravity.CENTER_VERTICAL }),
                LayoutPair(switchV.also { it.setPadding(0, dp2px(16f), 0, dp2px(16f)) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL; it.setMargins(0, 0, 0, 0); it.marginEnd = dp2px(25f)})
            )
        ).create(context).also {
            it.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_UP -> {
                        it.background = context.getDrawable(R.drawable.ic_main_bg)
                        switchV.also { switch ->
                            OwnSP.ownSP.edit().run {
                                if (OwnSP.ownSP.getBoolean(key, false)) putBoolean(key, false)
                                else putBoolean(key, true)
                                apply()
                            }
                            switch.isChecked = OwnSP.ownSP.getBoolean(key, false)
                        }
                    }
                    MotionEvent.ACTION_DOWN -> {
                        it.background = context.getDrawable(R.drawable.ic_main_down_bg)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        it.background = context.getDrawable(R.drawable.ic_main_down_bg)
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        it.background = context.getDrawable(R.drawable.ic_main_bg)
                    }
                }
                return@setOnTouchListener true
            }
        }
    }
}