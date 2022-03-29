package com.zhenxiang.blur

import android.view.View
import com.android.internal.graphics.drawable.BackgroundBlurDrawable

fun View.createBackgroundBlurDrawable(): BackgroundBlurDrawable? {
    return viewRootImpl?.createBackgroundBlurDrawable()
}