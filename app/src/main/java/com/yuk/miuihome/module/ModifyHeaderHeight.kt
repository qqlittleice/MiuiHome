package com.yuk.miuihome.module

import android.content.res.Resources
import com.yuk.miuihome.Config
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.dp2px
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class ModifyHeaderHeight {

    fun init() {
        val value = ownSP.getFloat("recents_task_view_header_height", -1f)
        if (value == -1f || value == 40f) return
        Resources::class.java.hookBeforeMethod(
            "getDimensionPixelSize",
            Int::class.javaPrimitiveType
        ) {
            if (it.args[0] == HomeContext.context.resources.getIdentifier(
                    "recents_task_view_header_height",
                    "dimen",
                    Config.hookPackage
                )
            ) {
                it.result = dp2px(value)
            }
        }
        Resources::class.java.hookBeforeMethod(
            "getDimensionPixelOffset",
            Int::class.javaPrimitiveType
        ) {
            if (it.args[0] == HomeContext.context.resources.getIdentifier(
                    "recents_task_view_header_height",
                    "dimen",
                    Config.hookPackage
                )
            ) {
                it.result = dp2px(value)
            }
        }
    }
}