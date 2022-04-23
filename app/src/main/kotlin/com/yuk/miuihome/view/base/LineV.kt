package com.yuk.miuihome.view.base

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.ktx.dp2px

class LineV : BaseView() {

    override fun getType(): BaseView = this

    override fun create(context: Context): View {
        return View(context).also {
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(0.9f))
            layoutParams.setMargins(dp2px(25f), dp2px(16f), dp2px(25f), dp2px(16f))
            it.layoutParams = layoutParams
            it.setBackgroundColor(context.resources.getColor(R.color.line, null))
        }
    }
}