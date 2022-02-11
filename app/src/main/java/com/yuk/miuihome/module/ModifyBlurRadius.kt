package com.yuk.miuihome.module

import android.view.Window
import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.*
import kotlin.math.max
import kotlin.math.min

class ModifyBlurRadius {

    fun init() {
        val value = OwnSP.ownSP.getFloat("blurRadius", -1f)
        if (value == -1f || value == 1f) return
        if (OwnSP.ownSP.getBoolean("appReturnAmin", false)) return
        "com.miui.home.launcher.common.BlurUtils".replaceMethod("fastBlur", Float::class.java, Window::class.java, Boolean::class.java, Long::class.java
        ) {
            val float = it.args[0] as Float
            val window = it.args[1] as Window
            val boolean = it.args[2] as Boolean
            val long = it.args[3] as Long
            var min: Float = min(value, max(0.0f, float))
            val blurClass = "com.miui.home.launcher.common.BlurUtils".findClass()
            val getCurrentBlurRatio = blurClass.callStaticMethod("getCurrentBlurRatio") as Float
            //val sBlurAnim  = blurClass.getStaticObjectField("sBlurAnim") as ValueAnimator
            val sBlurRatioValue: Float = blurClass.getStaticObjectField("sBlurRatioValue") as Float
            val sMinusOneBlurRatio: Float = blurClass.getStaticObjectField("sMinusOneBlurRatio") as Float
            if (min.toDouble() < 0.01) min = 0.0f
            //if (sBlurAnim.isRunning) sBlurAnim.cancel()
            if (sBlurRatioValue == min && sMinusOneBlurRatio == min) return@replaceMethod null
            if (boolean) {
                blurClass.callStaticMethod("startBlurAnim", window, getCurrentBlurRatio, min, long)
                //return@replaceMethod sBlurAnim
            }
            blurClass.callStaticMethod("fastBlurDirectly", min, window)
            return@replaceMethod null
        }
    }
}