package com.yuk.miuihome.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.CompoundButton
import com.yuk.miuihome.HomeContext
import android.widget.Switch
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.dip2px
import com.yuk.miuihome.utils.isNightMode

@SuppressLint("UseSwitchCompatOrMaterialCode")
class SettingSwitch(context: Context) : Switch(context) {
    private var color = if (isNightMode(getContext())) "#ffffff" else "#000000"
        set(value) = setTextColor(Color.parseColor(value))
    private val editor by lazy { ownSP.edit() }
    var toastText = ""
    var defState = false
    var key = ""
        set(value) {
            isChecked = ownSP.getBoolean(value, defState)
            setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
                if (b and (toastText != "")) LogUtil.toast(toastText)
                editor.putBoolean(value, b)
                editor.apply()
            }
        }

    init {
        setPadding(dip2px(10), dip2px(7), dip2px(10), dip2px(7))
        setTextColor(Color.parseColor(color))
    }

    class FastBuilder(
        private val mContext: Context = HomeContext.context,
        private val mText: String,
        private val mToastText: String? = null,
        private val mDefState: Boolean? = null,
        private val mKey: String,
        private val show: Boolean = true,
        private val mOnClickListener: ((View) -> Unit)? = null
    ) {
        fun build() = SettingSwitch(mContext).apply {
            text = mText
            mToastText?.let { toastText = it }
            mDefState?.let { defState = it }
            key = mKey
            mOnClickListener?.let { setOnClickListener(it) }
            if (!show) visibility = View.GONE
        }
    }
}