package com.yuk.miuihome.module

import android.graphics.RectF
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.callMethod
import com.yuk.miuihome.utils.ktx.callStaticMethod
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookAfterMethod

class ModifyTaskHorizontal {

    fun init() {
        val value1 = OwnSP.ownSP.getFloat("task_horizontal1", -1f)
        val value2 = OwnSP.ownSP.getFloat("task_horizontal2", -1f)
        if ((value1 == -1f || value2 == -1f) || (value1 == 1f && value2 == 1f)) return
        "com.miui.home.recents.views.TaskStackViewsAlgorithmHorizontal".hookAfterMethod("scaleTaskView", RectF::class.java,
        ) {
            "com.miui.home.recents.util.Utilities".findClass().callStaticMethod("scaleRectAboutCenter", it.args[0], if (it.thisObject.callMethod("isLandscapeVisually") as Boolean) value2 else value1)
        }
    }
}