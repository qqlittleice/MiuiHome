package com.yuk.miuihome.module.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import com.yuk.miuihome.utils.OwnSP

val emptyCheckChangedListener = CompoundButton.OnCheckedChangeListener { _, _ -> }
val emptyTextClickListener = View.OnClickListener { }

class SettingsSwitch(context: Context, attributeSet: AttributeSet): Switch(context, attributeSet) {

    private val editor by lazy { OwnSP.ownSP.edit() }
    private var customCheckedChangeListener: OnCheckedChangeListener? = null

    fun setCustomCheckedChangeListener(listener: OnCheckedChangeListener) = run { customCheckedChangeListener = listener }

    var key = ""
        set(value) {
            isChecked = OwnSP.ownSP.getBoolean(value, false)
            setOnCheckedChangeListener { a, b ->
                customCheckedChangeListener?.onCheckedChanged(a, b)
                editor.putBoolean(value, b)
                editor.apply()
            }
        }
}