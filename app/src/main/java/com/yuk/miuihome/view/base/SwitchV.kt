package com.yuk.miuihome.view.base

import android.content.Context
import android.view.View
import android.widget.CompoundButton
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.view.CustomSwitch

class SwitchV(
    private val key: String,
    val customOnCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null
) : BaseView() {

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return CustomSwitch(context).also {
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
    }
}