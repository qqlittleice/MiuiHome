package com.yuk.miuihome.module

import android.content.Context
import android.graphics.RectF
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*

class ModifyTaskVertical {

    fun init() {
        val value = OwnSP.ownSP.getFloat("task_vertical", 1000f) / 1000f
        if (value == 1f) return
        "com.miui.home.recents.views.TaskStackViewsAlgorithmVertical".replaceMethod("scaleTaskView", RectF::class.java
        ) {
            val context = it.thisObject.getObjectField("mContext") as Context
            "com.miui.home.recents.util.Utilities".findClass().callStaticMethod("scaleRectAboutCenter", it.args[0], "com.miui.home.recents.util.Utilities".findClass().callStaticMethod("getTaskViewScale", context))
            "com.miui.home.recents.util.Utilities".findClass().callStaticMethod("scaleRectAboutCenter", it.args[0], value - (context.resources.getDimensionPixelSize(context.resources.getIdentifier("recents_task_view_padding", "dimen", Config.hookPackage)) * value / it.args[0].callMethod("width") as Float))
        }
    }
}