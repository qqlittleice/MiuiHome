package com.yuk.miuihome.view.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.utils.ktx.sp2px

class ListPopupWindowAdapter(
    context: Context,
    data: ArrayList<String>,
    private val currentValue: String
) : BaseAdapter() {
    private val context: Context
    private val data: ArrayList<String>

    /**
     * 创建背景颜色
     *
     * @param color       填充色
     * @param strokeColor 线条颜色
     * @param strokeWidth 线条宽度  单位px
     * @param radius      角度  px,长度为4,分别表示左上,右上,右下,左下的角度
     */
    private fun createRectangleDrawable(color: Int, strokeColor: Int = 0, strokeWidth: Int, radius: FloatArray?): GradientDrawable {
        return try {
            GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(color)
                if (strokeColor != 0) setStroke(strokeWidth, strokeColor)
                if (radius != null && radius.size == 4) {
                    cornerRadii = floatArrayOf(
                        radius[0], radius[0], radius[1],
                        radius[1], radius[2], radius[2],
                        radius[3], radius[3]
                    )
                }
            }
        } catch (e: Exception) {
            GradientDrawable()
        }
    }

    /**
     * 创建点击颜色
     *
     * @param pressedDrawable 点击颜色
     * @param normalDrawable  正常颜色
     */
    private fun createStateListDrawable(pressedDrawable: GradientDrawable, normalDrawable: GradientDrawable): StateListDrawable {
        return StateListDrawable().apply {
            addState(intArrayOf(android.R.attr.state_focused), pressedDrawable)
            addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
            addState(intArrayOf(-android.R.attr.state_focused), normalDrawable)
        }
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val thisText = data[position]
        return convertView
            ?: LinearLayout(context).apply {
                var radius = floatArrayOf(0f, 0f, 0f, 0f)
                val radiusFloat = dp2px(context, 20f).toFloat()
                when (position) {
                    0 -> {
                        radius = floatArrayOf(radiusFloat, radiusFloat, 0f, 0f)
                    }
                    data.size - 1 -> {
                        radius = floatArrayOf(0f, 0f, radiusFloat, radiusFloat)
                    }
                }
                val pressedDrawable = createRectangleDrawable(context.getColor(if (currentValue == thisText) R.color.popup_select_click else R.color.popup_background_click), 0, 0, radius)
                val normalDrawable = createRectangleDrawable(context.getColor(if (currentValue == thisText) R.color.popup_select else R.color.popup_background), 0, 0, radius)
                background = createStateListDrawable(pressedDrawable, normalDrawable)
                addView(TextView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    textSize = sp2px(context, 5.5f)
                    setPadding(dp2px(context, 25f), dp2px(context, 18f), 0, dp2px(context, 18f))
                    isSingleLine = true
                    text = thisText
                })
                addView(ImageView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also {
                        it.gravity = Gravity.CENTER_VERTICAL
                        it.setMargins(0, dp2px(context, 2f), dp2px(context, 25f), 0)
                    }
                    background = context.getDrawable(R.drawable.ic_popup_select)
                    if (currentValue != thisText) visibility = View.GONE
                })
            }
    }

    init {
        this.context = context
        this.data = data
    }
}