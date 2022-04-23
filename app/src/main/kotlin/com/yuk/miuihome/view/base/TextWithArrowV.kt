package com.yuk.miuihome.view.base

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.view.data.LayoutPair

class TextWithArrowV(
    private val textWithSummaryV: TextWithSummaryV,
    private val onClickListener: View.OnClickListener? = null
) : BaseView() {

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return LinearContainerV(LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(textWithSummaryV.create(context).also { it.setPadding(0, 0, 0, 0) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)),
                LayoutPair(ImageView(context).also { view -> view.background = context.getDrawable(R.drawable.ic_right_arrow) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL; it.marginEnd = dp2px(25f) }),
            )
        ).create(context).also { view -> onClickListener?.let { view.setOnClickListener(it); view.background = context.getDrawable(R.drawable.ic_click_check); view.setPadding(0, dp2px(16f), 0, dp2px(16f))} }
    }
}