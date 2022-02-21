package com.yuk.miuihome.utils

import android.content.Context
import android.view.View
import com.yuk.miuihome.view.base.BaseView

object ViewBuilder {

    fun build(context: Context, view: BaseView): View? {
        if (view.hasLoad) return null
        return view.create(context).also { view.hasLoad = true }
    }
}