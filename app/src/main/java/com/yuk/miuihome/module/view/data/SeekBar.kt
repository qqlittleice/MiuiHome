package com.yuk.miuihome.module.view.data

import android.widget.TextView

data class SeekBar(
    val min: Int? = null,
    val max: Int? = null,
    val divide: Int = 1,
    val progress: Int? = null,
    val key: String = "",
    val callBacks: ((Int, TextView) -> Unit)? = null
)