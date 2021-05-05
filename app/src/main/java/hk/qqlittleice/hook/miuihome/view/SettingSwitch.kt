package hk.qqlittleice.hook.miuihome.view

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import hk.qqlittleice.hook.miuihome.HomeContext
import hk.qqlittleice.hook.miuihome.utils.LogUtil
import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.dp2px
import hk.qqlittleice.hook.miuihome.utils.isNightMode

class SettingSwitch(context: Context) : Switch(context) {
    var size: Float
        get() = textSize
        set(value) {
            textSize = value
        }
    var color = if (isNightMode(getContext())) "#ffffff" else "#ffffff" // #000000
        set(value) = setTextColor(Color.parseColor(value))
    var sharedPreferences = OwnSP.ownSP
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
        setPadding(dp2px(getContext(), 10f), dp2px(getContext(), 10f), dp2px(getContext(), 10f), dp2px(getContext(), 10f))
        setTextColor(Color.parseColor(color))
    }

    class Builder(private val mContext: Context = HomeContext.context, private val block: SettingSwitch.() -> Unit) {
        fun build() = SettingSwitch(mContext).apply(block)
    }

    class FastBuilder(private val mContext: Context = HomeContext.contextForView, private val mText: String, private val mToastText: String? = null, private val mDefaultState: Boolean? = null, private val mKey: String, private val mOnClickListener: ((View) -> Unit)? = null) {
        fun build() = SettingSwitch(mContext).apply {
            text = mText
            mToastText?.let { toastText = it }
            mDefaultState?.let { defaultState = it }
            key = mKey
            mOnClickListener?.let { setOnClickListener(it) }
        }
    }
}