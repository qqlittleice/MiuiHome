package com.yuk.miuihome.module

import android.graphics.RectF
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.OwnSP

class ModifyTaskHorizontal {

    fun init() {
        val value1 = OwnSP.ownSP.getFloat("task_horizontal1", -1f)
        val value2 = OwnSP.ownSP.getFloat("task_horizontal2", -1f)
        if ((value1 == -1f || value2 == -1f) || (value1 == 1f && value2 == 1f)) return
        findMethod("com.miui.home.recents.views.TaskStackViewsAlgorithmHorizontal") {
            name == "scaleTaskView" && parameterTypes[0] == RectF::class.java
        }.hookAfter {
            loadClass("com.miui.home.recents.util.Utilities").invokeStaticMethodAuto("scaleRectAboutCenter", it.args[0], if (it.thisObject.invokeMethodAuto("isLandscapeVisually") as Boolean) value2 else value1)
        }
    }
}