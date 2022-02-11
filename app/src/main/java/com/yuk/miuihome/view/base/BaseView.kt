package com.yuk.miuihome.view.base

import android.content.Context
import android.view.View

abstract class BaseView : BaseProperties {

    var hasLoad = false

    abstract fun getType(): BaseView

    abstract fun create(context: Context): View
}

interface BaseProperties {

    val outside: Boolean
    get() = false
}
