package com.yuk.miuihome.module.view.base

import android.content.Context
import android.view.View
import android.widget.TextView
import com.yuk.miuihome.module.view.data.Padding
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.utils.ktx.sp2px

class TextV(val text: String? = null, val resId: Int? = null, val textSize: Float? = null, val padding: Padding? = null, val onClickListener: View.OnClickListener? = null): BaseView() {

    private var myView: View? = null

    fun getMyView(context: Context): View = myView ?: innerCreate(context)

    override fun getType(): BaseView {
        return this
    }

    private fun innerCreate(context: Context): View {
        return TextView(context).also { view ->
            text?.let { view.text = it }
            resId?.let { view.setText(it) }
            if (textSize == null) {
                view.textSize = sp2px(context, 7.0f)
            } else {
                view.textSize = textSize
            }
            view.setPadding(0, dp2px(context, 15f), dp2px(context, 5f), dp2px(context, 15f))
            padding?.let { view.setPadding(it.left, it.top, it.right, it.bottom) }
            onClickListener?.let { view.setOnClickListener(it) }
            myView = view
        }
    }

    override fun create(context: Context): View {
        return getMyView(context)
    }
}