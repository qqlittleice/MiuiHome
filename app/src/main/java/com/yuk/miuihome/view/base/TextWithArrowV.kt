package com.yuk.miuihome.view.base

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.yuk.miuihome.R
import com.yuk.miuihome.view.data.LayoutPair
import com.yuk.miuihome.view.utils.ktx.dp2px

class TextWithArrowV(
    private val textV: TextV,
    val onClickListener: View.OnClickListener? = null
) : BaseView() {

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return LinearContainerV(
            LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(textV.create(context), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)),
                LayoutPair(ImageView(context).also { it.background = context.getDrawable(R.drawable.ic_right_arrow) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL })
            )
        ).create(context).also { view -> onClickListener?.let { view.setOnClickListener(it); view.background = context.getDrawable(R.drawable.ic_click_check) }; view.setPadding(0, 0, dp2px(25f), 0) }
    }
}