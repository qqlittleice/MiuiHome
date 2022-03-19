package com.yuk.miuihome.utils.ktx

import android.annotation.SuppressLint
import android.util.TypedValue
import com.github.kyuubiran.ezxhelper.init.InitFields

fun dp2px(dpValue: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, InitFields.moduleRes.displayMetrics).toInt()

fun px2dp(pxValue: Int): Int = (pxValue / InitFields.moduleRes.displayMetrics.density + 0.5f * if (pxValue >= 0) 1 else -1).toInt()

fun getDensityDpi(): Int = (InitFields.moduleRes.displayMetrics.widthPixels / InitFields.moduleRes.displayMetrics.density).toInt()

@SuppressLint("PrivateApi")
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun getProp(mKey: String): String = Class.forName("android.os.SystemProperties").getMethod("get", String::class.java).invoke(Class.forName("android.os.SystemProperties"), mKey).toString()
