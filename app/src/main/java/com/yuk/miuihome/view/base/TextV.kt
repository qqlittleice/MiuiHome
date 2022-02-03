package com.yuk.miuihome.view.base

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import com.yuk.miuihome.R
import com.yuk.miuihome.view.data.Padding
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.utils.ktx.sp2px

class TextV(
    val text: String? = null,
    val resId: Int? = null,
    val textSize: Float? = null,
    val textColor: Int? = null,
    val padding: Padding? = null,
    val typeface: Typeface? = null,
    val onClickListener: View.OnClickListener? = null
) : BaseView() {

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return TextView(context).also { view ->
            text?.let { view.text = it }
            resId?.let { view.setText(it) }
            if (textSize == null)
                view.textSize = sp2px(context, 6.5f)
            else
                view.textSize = textSize
            if (typeface == null)
                view.paint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            else
                view.paint.typeface = typeface
            textColor?.let { view.setTextColor(it) }
            view.setPadding(dp2px(context, 25f), dp2px(context, 16f), dp2px(context, 25f), dp2px(context, 16f))
            padding?.let { view.setPadding(it.left, it.top, it.right, it.bottom) }
            onClickListener?.let { view.setOnClickListener(it); view.background = context.getDrawable(R.drawable.ic_click_check) }
        }
    }
}