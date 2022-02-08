package com.yuk.miuihome.utils.ktx

import com.yuk.miuihome.utils.HomeContext
import kotlin.math.roundToInt

fun dp2px(dpValue: Float): Int = (dpValue * HomeContext.context.resources.displayMetrics.density).roundToInt()

fun px2dp(pxValue: Int): Int = (pxValue / HomeContext.context.resources.displayMetrics.density).roundToInt()

fun sp2px(spValue: Float): Float = (spValue * HomeContext.context.resources.displayMetrics.scaledDensity + 0.5f)
