package com.yuk.miuihome.module.view.data

import android.widget.CompoundButton

data class Switch(
    val key: String?,
    val onCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null
)
