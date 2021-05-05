package hk.qqlittleice.hook.miuihome.module

import android.content.res.Resources
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import hk.qqlittleice.hook.miuihome.Config
import hk.qqlittleice.hook.miuihome.HomeContext
import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.dp2px

class ModifyRoundedCorners {

    fun init() {

        val value = OwnSP.ownSP.getFloat("recents_task_view_rounded_corners_radius", -1f)
        if (value == -1f) return

        XposedHelpers.findAndHookMethod(Resources::class.java, "getDimensionPixelSize", Int::class.javaPrimitiveType, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (param!!.args[0] == HomeContext.context.resources.getIdentifier("recents_task_view_rounded_corners_radius_min", "dimen", Config.hookPackage)) {
                    param.result = dp2px(HomeContext.context, value)
                }
            }
        })

        XposedHelpers.findAndHookMethod(Resources::class.java, "getDimensionPixelSize", Int::class.javaPrimitiveType, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (param!!.args[0] == HomeContext.context.resources.getIdentifier("recents_task_view_rounded_corners_radius_max", "dimen", Config.hookPackage)) {
                    param.result = dp2px(HomeContext.context, value)
                }
            }
        })
    }

}