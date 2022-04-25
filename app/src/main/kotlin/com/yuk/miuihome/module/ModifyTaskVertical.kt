package com.yuk.miuihome.module

import android.graphics.RectF
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.OwnSP

class ModifyTaskVertical {

    fun init() {
        val value = OwnSP.ownSP.getFloat("task_vertical", 1000f) / 1000f
        if (value == 1f) return
        val utilitiesClass = loadClass("com.miui.home.recents.util.Utilities")
        findMethod("com.miui.home.recents.views.TaskStackViewsAlgorithmVertical") {
            name == "scaleTaskView" && parameterTypes[0] == RectF::class.java
        }.hookReplace {
            utilitiesClass.invokeStaticMethodAuto("scaleRectAboutCenter", it.args[0], value * utilitiesClass.invokeStaticMethodAuto("getTaskViewScale", appContext) as Float)
        }
    }
}