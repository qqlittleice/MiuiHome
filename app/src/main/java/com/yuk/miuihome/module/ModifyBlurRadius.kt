package com.yuk.miuihome.module

import android.animation.ValueAnimator
import android.view.Window
import com.yuk.miuihome.utils.ktx.*
import kotlin.math.max
import kotlin.math.min

class ModifyBlurRadius {

    fun init() {
        val value = 1.0f

        "com.miui.home.launcher.common.BlurUtils".replaceMethod(
            "fastBlur",
            Float::class.java,
            Window::class.java,
            Boolean::class.java,
            Long::class.java
        ) {
            val float = it.args[0] as Float
            val window = it.args[1] as Window
            val boolean = it.args[2] as Boolean
            val long = it.args[3] as Long
            var min: Float = min(value, max(0.0f, float))
            val blurClass = "com.miui.home.launcher.common.BlurUtils".findClass()
            val getCurrentBlurRatio = blurClass.callStaticMethod("getCurrentBlurRatio") as Float
            val sBlurAmin: ValueAnimator  = blurClass.getStaticObjectField("sBlurAnim") as ValueAnimator
            val sBlurRatioValue: Float = blurClass.getStaticObjectField("sBlurRatioValue") as Float
            val sMinusOneBlurRatio: Float = blurClass.getStaticObjectField("sMinusOneBlurRatio") as Float
            if (min.toDouble() < 0.01) min = 0.0f
            val valueAnimator: ValueAnimator = sBlurAmin
            if (valueAnimator.isRunning) sBlurAmin.cancel()
            if (sBlurRatioValue == min && sMinusOneBlurRatio == min) return@replaceMethod null
            if (boolean) {
                blurClass.callStaticMethod("startBlurAnim", window, getCurrentBlurRatio, min, long)
                return@replaceMethod sBlurAmin
            }
            blurClass.callStaticMethod("fastBlurDirectly", min, window)
            return@replaceMethod null
        }
    }
}