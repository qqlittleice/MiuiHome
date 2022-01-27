package com.yuk.miuihome.module.view.base

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.yuk.miuihome.module.view.data.LayoutPair

class TextWithSwitchV(val textV: TextV, val switchV: SwitchV): BaseView() {

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return LinearContainerV(
            LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(textV.create(context), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)),
                LayoutPair(switchV.create(context), LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL })
            )
        ).create(context)
    }
}