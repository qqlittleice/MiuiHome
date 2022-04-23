package com.yuk.miuihome.view.adapter

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.ktx.dp2px

class ListPopupWindowAdapter(
    context: Context,
    array: ArrayList<String>,
    private val currentValue: String?
) : BaseAdapter() {
    private val context: Context
    private val array: ArrayList<String>

    private fun createRectangleDrawable(color: Int, radius: FloatArray): GradientDrawable {
        return try {
            GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(color)
                setStroke(0, 0)
                if (radius.size == 4) {
                    cornerRadii = floatArrayOf(radius[0], radius[0], radius[1], radius[1], radius[2], radius[2], radius[3], radius[3])
                }
            }
        } catch (e: Exception) {
            GradientDrawable()
        }
    }

    private fun createStateListDrawable(pressedDrawable: GradientDrawable, normalDrawable: GradientDrawable): StateListDrawable {
        return StateListDrawable().apply {
            addState(intArrayOf(android.R.attr.state_focused), pressedDrawable)
            addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
            addState(intArrayOf(-android.R.attr.state_focused), normalDrawable)
        }
    }

    fun getWidth(): Int {
        var maxWidth = 0
        for (t in array) {
            val textView = TextView(context).also { it.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT) }
            textView.text = t
            textView.measure(0, 0)
            maxWidth = if (maxWidth > textView.measuredWidth) maxWidth else textView.measuredWidth
        }
        return maxWidth
    }

    override fun getCount(): Int = array.size

    override fun getItem(position: Int): Any = array[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return convertView
            ?: LinearLayout(context).apply {
                val thisText = array[position]
                orientation = LinearLayout.HORIZONTAL
                var radius = floatArrayOf(0f, 0f, 0f, 0f)
                val radiusFloat = dp2px(16f).toFloat()
                when (position) {
                    0 -> radius = floatArrayOf(radiusFloat, radiusFloat, 0f, 0f)
                    array.size - 1 -> radius = floatArrayOf(0f, 0f, radiusFloat, radiusFloat)
                }
                val pressedDrawable = createRectangleDrawable(context.getColor(if (currentValue == thisText) R.color.popup_select_click else R.color.popup_background_click), radius)
                val normalDrawable = createRectangleDrawable(context.getColor(if (currentValue == thisText) R.color.popup_select else R.color.popup_background), radius)
                background = createStateListDrawable(pressedDrawable, normalDrawable)
                addView(TextView(context).apply {
                    text = thisText
                    isSingleLine = true
                    setTextSize(TypedValue.COMPLEX_UNIT_SP,16f)
                    typeface = Typeface.create(null, 500, false)
                    setPadding(0, dp2px(18f), 0, dp2px(18f))
                    if (currentValue == thisText) setTextColor(context.getColor(R.color.popup_select_text))
                }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).also { it.gravity = Gravity.START; it.marginStart = dp2px(25f); it.marginEnd = dp2px(25f) })
                addView(ImageView(context).apply {
                    if (currentValue == thisText) background = context.getDrawable(R.drawable.ic_popup_select)
                }, LinearLayout.LayoutParams(dp2px(15f), dp2px(15f)).also { it.gravity = Gravity.CENTER_VERTICAL; it.marginEnd = dp2px(25f) })
            }
    }

    init {
        this.context = context
        this.array = array
    }
}