package com.yuk.miuihome.module.view.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.ColorInt
import com.yuk.miuihome.R
import com.yuk.miuihome.module.view.data.LayoutPair
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.utils.ktx.sp2px


class TextWithSpinnerV(private val textV: TextV, val key: String, var select: String?, val array: ArrayList<String>, private val callBacks: ((String) -> Unit)? = null): BaseView() {

    override var outside = true

    override fun getType(): BaseView = this

    @SuppressLint("RtlHardcoded")
    override fun create(context: Context): View {
        val text = TextView(context)
        val popup = ListPopupWindow(context)
        popup.setBackgroundDrawable(context.getDrawable(R.drawable.rounded_corners_pop))
        popup.setAdapter(ArrayAdapter(context, android.R.layout.simple_list_item_1, array))
        popup.verticalOffset = dp2px(context,-80f)
        popup.width = dp2px(context, 120f)
        popup.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popup.isModal = true
        popup.setOnItemClickListener { parent, _, position, _ ->
            val a = parent.getItemAtPosition(position).toString()
            text.text = a
            callBacks?.let { it -> it(a) }
            popup.dismiss()
        }
        val spinner = LinearContainerV(
            LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(text.also { it.setTextColor(context.getColor(R.color.spinner)); it.text = select; it.setPadding(dp2px(context, 30f), 0, dp2px(context, 5f), 0); it.textSize = sp2px(context, 5.8f) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).also { it.gravity = Gravity.CENTER_VERTICAL + Gravity.RIGHT}),
                LayoutPair(ImageView(context).also { it.background = context.getDrawable(R.drawable.ic_up_down) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL })
            )
        )
        return LinearContainerV(
            LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(textV.create(context).also { it.setPadding(dp2px(context, 25f), 0, dp2px(context, 25f), 0) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)),
                LayoutPair(spinner.create(context).also { it.setPadding(0, 0, dp2px(context, 25f), 0) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL })
            )
        ).create(context).also { view ->
            view.setOnClickListener {
                popup.horizontalOffset = dp2px(context,-24f)
                popup.setDropDownGravity(Gravity.RIGHT)
                popup.anchorView = it
                popup.show()
            }
        }
    }
}