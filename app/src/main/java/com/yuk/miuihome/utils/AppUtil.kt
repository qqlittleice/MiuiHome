package com.yuk.miuihome.utils

import android.content.Context
import android.content.res.Configuration
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext

fun dp2px(context: Context, dpValue: Float): Int =
    (dpValue * context.resources.displayMetrics.density + 0.5f).toInt()

fun dip2px(dpValue: Int): Int =
    (dpValue * appContext.resources.displayMetrics.density + 0.5f).toInt()

fun sp2px(context: Context, spValue: Float): Int =
    (spValue * context.resources.displayMetrics.scaledDensity + 0.5f).toInt()


fun px2dip(pxValue: Int): Int =
    (pxValue / appContext.resources.displayMetrics.density + 0.5f).toInt()

fun isNightMode(context: Context): Boolean =
    (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
