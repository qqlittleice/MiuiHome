package com.yuk.miuihome.module.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.Switch
import com.yuk.miuihome.utils.OwnSP

@SuppressLint("UseSwitchCompatOrMaterialCode")
class SettingsSwitch(context: Context, attributeSet: AttributeSet): Switch(context, attributeSet) {

    private val editor by lazy { OwnSP.ownSP.edit() }
    var customCheckedChangeListener: OnCheckedChangeListener? = null

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