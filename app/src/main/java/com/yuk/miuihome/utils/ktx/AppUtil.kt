package com.yuk.miuihome.utils.ktx

import android.util.TypedValue
import com.yuk.miuihome.utils.HomeContext


fun dp2px(dpValue: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, HomeContext.context.resources.displayMetrics).toInt()

fun px2dp(pxValue: Int): Int =  (pxValue / HomeContext.context.resources.displayMetrics.density + 0.5f * if (pxValue >= 0) 1 else -1).toInt()