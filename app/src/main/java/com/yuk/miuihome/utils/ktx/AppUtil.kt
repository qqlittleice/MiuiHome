package com.yuk.miuihome.utils.ktx

import android.annotation.SuppressLint
import android.util.TypedValue
import com.github.kyuubiran.ezxhelper.init.InitFields

fun dp2px(dpValue: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, InitFields.appContext.resources.displayMetrics).toInt()

fun px2dp(pxValue: Int): Int = (pxValue / InitFields.appContext.resources.displayMetrics.density + 0.5f * if (pxValue >= 0) 1 else -1).toInt()

fun getDensityDpi(): Int = (InitFields.appContext.resources.displayMetrics.widthPixels / InitFields.appContext.resources.displayMetrics.density).toInt()

@SuppressLint("PrivateApi")
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun getProp(mKey: String): String = Class.forName("android.os.SystemProperties").getMethod("get", String::class.java).invoke(Class.forName("android.os.SystemProperties"), mKey).toString()
