package com.yuk.miuihome.view.base

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.view.data.LayoutPair

class TextWithSwitchV(
    private val textWithSummaryV: TextWithSummaryV,
    private val switchV: SwitchV
) : BaseView() {

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return LinearContainerV(
            LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(textWithSummaryV.create(context).also { it.setPadding(0, dp2px(16f), 0, dp2px(16f)) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).also { it.gravity = Gravity.CENTER_VERTICAL }),
                LayoutPair(switchV.create(context).also { it.setPadding(0, dp2px(16f), 0, dp2px(16f)) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL; it.setMargins(0, 0, dp2px(25f), 0) })
            )
        ).create(context)
    }
}