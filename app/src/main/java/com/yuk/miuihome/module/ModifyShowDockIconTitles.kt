package com.yuk.miuihome.module

import android.content.Context
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import com.yuk.miuihome.utils.ktx.setReturnConstant
import de.robv.android.xposed.XposedHelpers
import kotlin.math.roundToInt

class ModifyShowDockIconTitles {

    fun init() {
        if (ownSP.getBoolean("showDockIconTitles", false)) {
            "com.miui.home.launcher.DeviceConfig".setReturnConstant(
                "isHotseatsAppTitleHided",
                result = false
            )
            "com.miui.home.launcher.DeviceConfig".hookAfterMethod(
                "calcHotSeatsHeight",
                Context::class.java,
                Boolean::class.java
            ) {
                val height = it.result as Int
                val sIsImmersiveNavigationBar = XposedHelpers.getStaticBooleanField(
                    "com.miui.home.launcher.DeviceConfig".findClass(),
                    "sIsImmersiveNavigationBar"
                )
                if (sIsImmersiveNavigationBar) it.result =
                    (height + 8 * HomeContext.context.resources.displayMetrics.density).roundToInt()
            }
        }
    }
}