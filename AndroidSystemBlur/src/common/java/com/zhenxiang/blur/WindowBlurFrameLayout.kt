package com.zhenxiang.blur

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import com.zhenxiang.blur.model.CornersRadius

class WindowBlurFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    val blurController: SystemBlurController

    init {
        val a = attrs?.let { context.obtainStyledAttributes(attrs, R.styleable.WindowBlurFrameLayout, defStyleAttr, defStyleRes) }
        if (a != null) {
            val allEdgesCornerRadius = a.getDimensionPixelSize(R.styleable.WindowBlurFrameLayout_cornerRadius, 0)
            val cornerRadius = CornersRadius(
                formatEdgeCornerRadius(allEdgesCornerRadius, a.getDimensionPixelSize(R.styleable.WindowBlurFrameLayout_cornerRadiusTopLeft, -1)),
                formatEdgeCornerRadius(allEdgesCornerRadius, a.getDimensionPixelSize(R.styleable.WindowBlurFrameLayout_cornerRadiusTopRight, -1)),
                formatEdgeCornerRadius(allEdgesCornerRadius, a.getDimensionPixelSize(R.styleable.WindowBlurFrameLayout_cornerRadiusBottomLeft, -1)),
                formatEdgeCornerRadius(allEdgesCornerRadius, a.getDimensionPixelSize(R.styleable.WindowBlurFrameLayout_cornerRadiusBottomRight, -1))
            )
            blurController = SystemBlurController(
                this,
                a.getColor(R.styleable.WindowBlurFrameLayout_backgroundColour, Color.TRANSPARENT),
                a.getFloat(R.styleable.WindowBlurFrameLayout_blurBackgroundColourOpacity, SystemBlurController.DEFAULT_BLUR_BACKGROUND_COLOUR_OPACITY),
                a.getInteger(R.styleable.WindowBlurFrameLayout_blurRadius, SystemBlurController.DEFAULT_BLUR_RADIUS),
                cornerRadius,
            )
            clipToOutline = a.getBoolean(R.styleable.WindowBlurFrameLayout_clipToOutline, false)
            a.recycle()
        } else {
            blurController = SystemBlurController(this)
        }
    }
} 
