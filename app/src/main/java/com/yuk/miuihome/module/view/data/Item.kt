package com.yuk.miuihome.module.view.data

import android.view.View

data class Item(
    val text: Text? = null,
    val switch: Switch? = null,
    val customItems: List<View> = arrayListOf(),
    val line: Boolean = false,
    val seekBar: SeekBar? = null
)
