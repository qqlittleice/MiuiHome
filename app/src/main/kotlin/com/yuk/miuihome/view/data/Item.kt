package com.yuk.miuihome.view.data

import android.view.View
import com.yuk.miuihome.view.base.BaseView

data class Item(
    val customItems: List<View> = arrayListOf(),
    val list: List<BaseView> = arrayListOf()
)
