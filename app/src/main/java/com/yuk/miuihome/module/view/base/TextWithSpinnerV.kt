package com.yuk.miuihome.module.view.base

import android.content.Context
import android.view.View
import android.widget.Spinner
import android.widget.TextView

class TextWithSpinnerV(val title: String, val key: String, val callBacks: ((Int, TextView) -> Unit)? = null): BaseView() {

    override var outside = true

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        val spinner = Spinner(context).also { view ->
            //TODO
        }
        return LinearContainerV(
            LinearContainerV.HORIZONTAL,
            arrayOf(
                //TODO
              )
        ).create(context)
    }
}