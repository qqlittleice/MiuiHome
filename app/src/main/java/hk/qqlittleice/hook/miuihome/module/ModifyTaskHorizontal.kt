package hk.qqlittleice.hook.miuihome.module

import android.content.Context
import android.graphics.RectF
import hk.qqlittleice.hook.miuihome.Config
import hk.qqlittleice.hook.miuihome.utils.LogUtil
import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.ktx.callMethod
import hk.qqlittleice.hook.miuihome.utils.ktx.callStaticMethod
import hk.qqlittleice.hook.miuihome.utils.ktx.getObjectField
import hk.qqlittleice.hook.miuihome.utils.ktx.replaceMethod

class ModifyTaskHorizontal {
    
    fun init() {
        val value = OwnSP.ownSP.getFloat("task_horizontal", -1f)
        if (value == -1f) return

        "com.miui.home.recents.views.TaskStackViewsAlgorithmHorizontal".replaceMethod("scaleTaskView", RectF::class.java) {
            "com.miui.home.recents.util.Utilities".callStaticMethod(
                "scaleRectAboutCenter",
                it.args[0],
                value)
        }
    }
}