package com.yuk.miuihome.view.base

import android.content.Context
import android.graphics.Typeface
import android.view.View
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.view.data.Padding

class SubtitleV(
    val text: String? = null,
    val resId: Int? = null,
    private val onClickListener: View.OnClickListener? = null
) : BaseView() {

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return TextV(text, resId, 13f, context.getColor(R.color.title), Padding(dp2px(25f), dp2px(13f), dp2px(25f), dp2px(13f)), Typeface.create(null, 500, false), onClickListener).create(context)
    }
}