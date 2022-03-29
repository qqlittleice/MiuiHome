package com.zhenxiang.blur

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import com.zhenxiang.blur.model.CornersRadius

class WindowBlurLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val blurController: SystemBlurController

    init {
        val a = attrs?.let { context.obtainStyledAttributes(attrs, R.styleable.WindowBlurLinearLayout, defStyleAttr, defStyleRes) }
        if (a != null) {
            val allEdgesCornerRadius = a.getDimensionPixelSize(R.styleable.WindowBlurLinearLayout_cornerRadius, 0)
            val cornerRadius = CornersRadius(
                formatEdgeCornerRadius(allEdgesCornerRadius, a.getDimensionPixelSize(R.styleable.WindowBlurLinearLayout_cornerRadiusTopLeft, -1)),
                formatEdgeCornerRadius(allEdgesCornerRadius, a.getDimensionPixelSize(R.styleable.WindowBlurLinearLayout_cornerRadiusTopRight, -1)),
                formatEdgeCornerRadius(allEdgesCornerRadius, a.getDimensionPixelSize(R.styleable.WindowBlurLinearLayout_cornerRadiusBottomLeft, -1)),
                formatEdgeCornerRadius(allEdgesCornerRadius, a.getDimensionPixelSize(R.styleable.WindowBlurLinearLayout_cornerRadiusBottomRight, -1))
            )
            blurController = SystemBlurController(
                this,
                a.getColor(R.styleable.WindowBlurLinearLayout_backgroundColour, Color.TRANSPARENT),
                a.getFloat(R.styleable.WindowBlurLinearLayout_blurBackgroundColourOpacity, SystemBlurController.DEFAULT_BLUR_BACKGROUND_COLOUR_OPACITY),
                a.getInteger(R.styleable.WindowBlurLinearLayout_blurRadius, SystemBlurController.DEFAULT_BLUR_RADIUS),
                cornerRadius,
            )
            clipToOutline = a.getBoolean(R.styleable.WindowBlurLinearLayout_clipToOutline, false)
            a.recycle()
        } else {
            blurController = SystemBlurController(this)
        }
    }
} 
