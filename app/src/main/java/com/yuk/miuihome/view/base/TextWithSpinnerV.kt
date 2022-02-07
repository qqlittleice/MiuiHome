package com.yuk.miuihome.view.base

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.utils.ktx.sp2px
import com.yuk.miuihome.view.adapter.ListPopupWindowAdapter
import com.yuk.miuihome.view.data.DataHelper
import com.yuk.miuihome.view.data.LayoutPair
import de.robv.android.xposed.XposedBridge

class TextWithSpinnerV(
    private val textV: TextV,
    var select: String?,
    val array: ArrayList<String>,
    private val callBacks: ((String) -> Unit)? = null
) : BaseView() {

    override var outside = true

    override fun getType(): BaseView = this

    private fun setBackgroundAlpha(bgAlpha: Float) {
        val lp = DataHelper.currentActivity.window.attributes
        lp.alpha = bgAlpha
        DataHelper.currentActivity.window.attributes = lp
    }

    @SuppressLint("RtlHardcoded", "ClickableViewAccessibility")
    override fun create(context: Context): View {
        val text = TextView(context)
        val popup = ListPopupWindow(context)
        popup.apply {
            setBackgroundDrawable(context.getDrawable(R.drawable.rounded_corners_pop))
            setAdapter(ListPopupWindowAdapter(context, array, select.toString()))
            verticalOffset = dp2px(context, -100f)
            width = dp2px(context, 160f)
            isModal = true
            setOnItemClickListener { parent, _, position, _ ->
                val p0 = parent.getItemAtPosition(position).toString()
                text.text = p0
                setAdapter(ListPopupWindowAdapter(context,array,p0))
                callBacks?.let { it -> it(p0) }
                dismiss()
            }
            setOnDismissListener {
                val animator = ValueAnimator.ofFloat(0.7f, 1f).setDuration(300)
                animator.addUpdateListener { animation -> setBackgroundAlpha(animation.animatedValue as Float) }
                animator.start()
            }
        }
        val spinner = LinearContainerV(
            LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(text.also { it.setTextColor(context.getColor(R.color.spinner)); it.text = select; it.setPadding(dp2px(context, 30f), 0, dp2px(context, 6f), 0); it.textSize = sp2px(context, 5.6f) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).also { it.gravity = Gravity.CENTER_VERTICAL + Gravity.RIGHT }),
                LayoutPair(ImageView(context).also { it.background = context.getDrawable(R.drawable.ic_up_down) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL })
            )
        )
        return LinearContainerV(
            LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(textV.create(context).also { it.setPadding(dp2px(context, 25f), 0, dp2px(context, 25f), 0) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)),
                LayoutPair(spinner.create(context).also { it.setPadding(0, 0, dp2px(context, 25f), 0) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL })
            )
        ).create(context).also {
            it.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_UP -> {
                        val animator = ValueAnimator.ofFloat(1f, 0.7f).setDuration(300)
                        animator.addUpdateListener { animation -> setBackgroundAlpha(animation.animatedValue as Float) }
                        animator.start()
                        val halfWidth = view.width / 2
                        if (halfWidth >= motionEvent.x) {
                            popup.apply {
                                horizontalOffset = dp2px(context, 25f)
                                setDropDownGravity(Gravity.LEFT)
                                anchorView = view
                                show()
                            }
                        } else {
                            popup.apply {
                                horizontalOffset = dp2px(context, -25f)
                                setDropDownGravity(Gravity.RIGHT)
                                anchorView = view
                                show()
                            }
                        }
                        it.background = context.getDrawable(R.drawable.ic_main_bg)
                    }
                    MotionEvent.ACTION_DOWN -> {
                        it.background = context.getDrawable(R.drawable.ic_main_down_bg)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        it.background = context.getDrawable(R.drawable.ic_main_down_bg)
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        it.background = context.getDrawable(R.drawable.ic_main_bg)
                    }
                }
                return@setOnTouchListener true
            }
            it.setPadding(0, dp2px(context, 16f), 0, dp2px(context, 16f))
        }
    }
}
