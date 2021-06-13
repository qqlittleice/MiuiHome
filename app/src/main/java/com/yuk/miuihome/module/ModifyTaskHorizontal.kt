package com.yuk.miuihome.module

import android.graphics.RectF
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.callStaticMethod
import com.yuk.miuihome.utils.ktx.replaceMethod

class ModifyTaskHorizontal {
    
    fun init() {

        val value = OwnSP.ownSP.getFloat("task_horizontal", -1f)
        if (value == -1f) return

        "com.miui.home.recents.views.TaskStackViewsAlgorithmHorizontal".replaceMethod("scaleTaskView", RectF::class.java) {
            "com.miui.home.recents.util.Utilities".callStaticMethod("scaleRectAboutCenter", it.args[0], value)}
    }
}