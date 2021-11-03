package com.yuk.miuihome.module

import android.content.res.Resources
import com.yuk.miuihome.Config
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.dp2px
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class ModifyRoundedCorners {

    fun init() {
        val value = ownSP.getFloat("recents_task_view_rounded_corners_radius", -1f)
        if (value == -1f || value == 20f) return
        Resources::class.java.hookBeforeMethod(
            "getDimensionPixelSize",
            Int::class.javaPrimitiveType
        ) {
            if (it.args[0] == HomeContext.context.resources.getIdentifier(
                    "recents_task_view_rounded_corners_radius_min",
                    "dimen",
                    Config.hookPackage
                )
            ) {
                it.result = dp2px(value)
            }
        }
        Resources::class.java.hookBeforeMethod(
            "getDimensionPixelSize",
            Int::class.javaPrimitiveType
        ) {
            if (it.args[0] == HomeContext.context.resources.getIdentifier(
                    "recents_task_view_rounded_corners_radius_max",
                    "dimen",
                    Config.hookPackage
                )
            ) {
                it.result = dp2px(value)
            }
        }
    }
}