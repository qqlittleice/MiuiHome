package com.yuk.miuihome.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.CompoundButton
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.dp2px
import com.yuk.miuihome.utils.isNightMode
import android.widget.Switch

@SuppressLint("UseSwitchCompatOrMaterialCode")
class SettingSwitch(context: Context) : Switch(context) {
    var size: Float
        get() = textSize
        set(value) {
            textSize = value
        }

    private var color = if (isNightMode(getContext())) "#ffffff" else "#000000"
        set(value) = setTextColor(Color.parseColor(value))
    private var sharedPreferences = OwnSP.ownSP
    private val editor by lazy { sharedPreferences.edit() }
    var toastText = ""
    var defaultState = false
    var key = ""
        set(value) {
            isChecked = sharedPreferences.getBoolean(value, defaultState)
            setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                if (b and (toastText != "")) LogUtil.toast(toastText)
                editor.putBoolean(value, b)
                editor.apply()
            }
        }

    init {
        setPadding(
            dp2px(getContext(), 10f),
            dp2px(getContext(), 5f),
            dp2px(getContext(), 10f),
            dp2px(getContext(), 5f)
        )
        setTextColor(Color.parseColor(color))
    }

    class Builder(
        private val mContext: Context = HomeContext.context,
        private val block: SettingSwitch.() -> Unit
    ) {
        fun build() = SettingSwitch(mContext).apply(block)
    }

    class FastBuilder(
        private val mContext: Context = HomeContext.context,
        private val mText: String,
        private val mToastText: String? = null,
        private val mDefaultState: Boolean? = null,
        private val mKey: String,
        private val show: Boolean = true,
        private val mOnClickListener: ((View) -> Unit)? = null
    ) {
        fun build() = SettingSwitch(mContext).apply {
            text = mText
            mToastText?.let { toastText = it }
            mDefaultState?.let { defaultState = it }
            key = mKey
            mOnClickListener?.let { setOnClickListener(it) }
            if (!show) visibility = View.GONE
        }
    }
}