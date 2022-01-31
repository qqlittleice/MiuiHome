package com.yuk.miuihome.module.view.base

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.yuk.miuihome.module.view.data.LayoutPair

class LinearContainerV(val orientation: Int, private val pairs: Array<LayoutPair>): BaseView() {
    companion object {
        const val VERTICAL = LinearLayout.VERTICAL
        const val HORIZONTAL = LinearLayout.HORIZONTAL
    }

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return LinearLayout(context).also {
            it.orientation = orientation
            for (pair in pairs) {
                it.addView(pair.view, pair.layoutParams)
            }
        }
    }
}