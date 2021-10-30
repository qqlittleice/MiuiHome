package com.yuk.miuihome.module

import android.content.res.Resources
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import com.yuk.miuihome.Config
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.dp2px

class ModifyHeaderHeight {

    fun init() {
        val value = ownSP.getFloat("recents_task_view_header_height", -1f)
        if (value == -1f) return

        XposedHelpers.findAndHookMethod(
            Resources::class.java,
            "getDimensionPixelSize",
            Int::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    if (param!!.args[0] == HomeContext.context.resources.getIdentifier(
                            "recents_task_view_header_height",
                            "dimen",
                            Config.hookPackage
                        )
                    ) {
                        param.result = dp2px(HomeContext.context, value)
                    }
                }
            })

        XposedHelpers.findAndHookMethod(
            Resources::class.java,
            "getDimensionPixelOffset",
            Int::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    if (param!!.args[0] == HomeContext.context.resources.getIdentifier(
                            "recents_task_view_header_height",
                            "dimen",
                            Config.hookPackage
                        )
                    ) {
                        param.result = dp2px(HomeContext.context, value)
                    }
                }
            })
    }
}