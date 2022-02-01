package com.yuk.miuihome.module.view.base

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.yuk.miuihome.R
import com.yuk.miuihome.module.view.data.LayoutPair

class TextWithArrowV(private val textV: TextV, val onClickListener: View.OnClickListener? = null): BaseView() {

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return LinearContainerV(
            LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(textV.create(context), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)),
                LayoutPair(ImageView(context).also { it.background = context.getDrawable(R.drawable.ic_right_arrow) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL })
            )
        ).create(context).also { view ->
            onClickListener?.let { view.setOnClickListener(it) }
        }
    }
}