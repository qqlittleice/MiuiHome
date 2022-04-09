package com.yuk.miuihome.module

import android.graphics.RectF
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*

class ModifyTaskVertical {

    fun init() {
        val value = OwnSP.ownSP.getFloat("task_vertical", 1000f) / 1000f
        if (value == 1f) return
        "com.miui.home.recents.views.TaskStackViewsAlgorithmVertical".replaceMethod("scaleTaskView", RectF::class.java
        ) {
            "com.miui.home.recents.util.Utilities".findClass().callStaticMethod("scaleRectAboutCenter", it.args[0], value * "com.miui.home.recents.util.Utilities".findClass().callStaticMethod("getTaskViewScale", appContext) as Float)
        }
    }
}