package com.yuk.miuihome.module.view.data

import android.view.View
import com.yuk.miuihome.module.view.base.BaseView

data class Item(
    val text: Text? = null,
    val switch: Switch? = null,
    val customItems: List<View> = arrayListOf(),
    val line: Boolean = false,
    val seekBar: SeekBar? = null,
    val spinner: Spinner? = null,
    val test: List<BaseView> = arrayListOf()
)
