package com.yuk.miuihome.module.view.data

import android.view.View
import com.yuk.miuihome.module.view.base.BaseView

data class Item(
    val customItems: List<View> = arrayListOf(),
    val line: Boolean = false,
    val test: List<BaseView> = arrayListOf()
)
