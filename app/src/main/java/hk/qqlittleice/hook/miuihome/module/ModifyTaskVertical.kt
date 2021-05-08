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

class ModifyTaskVertical {
    
    fun init() {

        val value = OwnSP.ownSP.getFloat("task_vertical", -1f)
        if (value == -1f) return

        "com.miui.home.recents.views.TaskStackViewsAlgorithmVertical".replaceMethod("scaleTaskView", RectF::class.java) {
            val context = it.thisObject.getObjectField("mContext") as Context

            "com.miui.home.recents.util.Utilities".callStaticMethod(
                "scaleRectAboutCenter",
                it.args[0],
                "com.miui.home.recents.util.Utilities".callStaticMethod("getTaskViewScale", context))

            "com.miui.home.recents.util.Utilities".callStaticMethod(
                "scaleRectAboutCenter",
                it.args[0],
                value - (context.resources.getDimensionPixelSize(context.resources.getIdentifier("recents_task_view_padding", "dimen", Config.hookPackage)) * value / it.args[0].callMethod("width") as Float))
        }
    }

}