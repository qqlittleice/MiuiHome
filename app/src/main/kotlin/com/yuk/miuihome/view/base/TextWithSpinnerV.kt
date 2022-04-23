package com.yuk.miuihome.view.base

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListPopupWindow
import android.widget.TextView
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.view.adapter.ListPopupWindowAdapter
import com.yuk.miuihome.view.data.DataHelper
import com.yuk.miuihome.view.data.LayoutPair

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

    @SuppressLint("ClickableViewAccessibility", "RtlHardcoded")
    override fun create(context: Context): View {
        val text = TextView(context)
        val popup = ListPopupWindow(context).apply {
            setBackgroundDrawable(context.getDrawable(R.drawable.rounded_corners_pop))
            setAdapter(ListPopupWindowAdapter(context, array, select.toString()))
            verticalOffset = dp2px(-100f)
            width = ListPopupWindowAdapter(context, array, select.toString()).getWidth() + dp2px(125f)
            isModal = true
            setOnItemClickListener { parent, _, position, _ ->
                val p0 = parent.getItemAtPosition(position).toString()
                text.text = p0
                setAdapter(ListPopupWindowAdapter(context, array, p0))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) parent.performHapticFeedback(HapticFeedbackConstants.CONFIRM, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                else parent.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                callBacks?.let { it -> it(p0) }
                dismiss()
            }
            setOnDismissListener {
                val animator = ValueAnimator.ofFloat(0.7f, 1f).setDuration(300)
                animator.addUpdateListener { animation -> setBackgroundAlpha(animation.animatedValue as Float) }
                animator.start()
            }
        }
        val spinner = LinearContainerV(LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(text.also { it.textAlignment = View.TEXT_ALIGNMENT_VIEW_START; it.setTextColor(context.getColor(R.color.spinner)); it.text = select; it.textSize = 16f }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).also { it.marginStart = dp2px(30f); it.marginEnd = dp2px(6f); it.gravity = Gravity.CENTER_VERTICAL + Gravity.END }),
                LayoutPair(ImageView(context).also { it.background = context.getDrawable(R.drawable.ic_up_down) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL })
            )
        )
        return LinearContainerV(LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(textV.create(context).also { it.setPadding(dp2px(25f), 0, dp2px(25f), 0) }, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)),
                LayoutPair(spinner.create(context).also { it.setPadding(0, 0, 0, 0)}, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL; it.marginEnd = dp2px(25f) })
            )
        ).create(context).also {
            it.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_UP -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) it.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                        else it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
                        val animator = ValueAnimator.ofFloat(1f, 0.7f).setDuration(300)
                        animator.addUpdateListener { animation -> setBackgroundAlpha(animation.animatedValue as Float) }
                        animator.start()
                        val halfWidth = view.width / 2
                        if (halfWidth >= motionEvent.x) {
                            popup.apply {
                                horizontalOffset = dp2px(20f)
                                setDropDownGravity(Gravity.LEFT)
                                anchorView = view
                                show()
                            }
                        } else {
                            popup.apply {
                                horizontalOffset = dp2px(-20f)
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
            it.setPadding(0, dp2px(16f), 0, dp2px(16f))
        }
    }
}