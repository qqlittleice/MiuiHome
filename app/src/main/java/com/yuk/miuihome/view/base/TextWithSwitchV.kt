package com.yuk.miuihome.view.base

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.yuk.miuihome.view.data.LayoutPair
import com.yuk.miuihome.utils.ktx.dp2px

class TextWithSwitchV(
    private val textV: TextV,
    private val switchV: SwitchV
) : BaseView() {

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return LinearContainerV(
            LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(textV.create(context).also { it.setPadding(dp2px(25f), dp2px(16f), dp2px(5f), dp2px(16f)) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)),
                LayoutPair(switchV.create(context).also { it.setPadding(0, dp2px(16f), 0, dp2px(16f)) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL; it.setMargins(0, 0, dp2px(25f), 0) })
            )
        ).create(context)
    }
}