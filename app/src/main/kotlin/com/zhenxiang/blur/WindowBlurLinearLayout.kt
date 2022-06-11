package com.zhenxiang.blur

import android.content.Context
import android.widget.LinearLayout

class WindowBlurLinearLayout constructor(context: Context) : LinearLayout(context) {
    val blurController: SystemBlurController = SystemBlurController(this)
}