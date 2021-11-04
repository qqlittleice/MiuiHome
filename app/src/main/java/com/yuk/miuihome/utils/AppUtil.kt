package com.yuk.miuihome.utils

import android.content.Context
import android.content.res.Configuration
import com.yuk.miuihome.HomeContext

fun dp2px(dpValue: Float): Int =
    (dpValue * HomeContext.context.resources.displayMetrics.density + 0.5f).toInt()

fun dip2px(dpValue: Int): Int =
    (dpValue * HomeContext.context.resources.displayMetrics.density + 0.5f).toInt()

fun sp2px(spValue: Float): Float =
    (spValue * HomeContext.context.resources.displayMetrics.scaledDensity + 0.5f)

fun px2dip(pxValue: Int): Int =
    (pxValue / HomeContext.context.resources.displayMetrics.density + 0.5f).toInt()

fun isNightMode(context: Context): Boolean =
    (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES