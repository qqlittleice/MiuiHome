package com.yuk.miuihome.module

import android.content.Context
import com.yuk.miuihome.ResourcesHook
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ResourcesHookData
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import de.robv.android.xposed.XposedHelpers
import kotlin.math.roundToInt


class TestCode2 {

    fun init() {
        if (OwnSP.ownSP.getBoolean("showDockTitles", false)) {
            ResourcesHook.hookMap["config_hide_hotseats_app_title"] = ResourcesHookData("bool", false)

            "com.miui.home.launcher.DeviceConfig".hookAfterMethod(
                "calcHotSeatsHeight",
                Context::class.java,
                Boolean::class.java
            ) {
                val context: Context = it.args[0] as Context
                val height = it.result as Int
                val sIsImmersiveNavigationBar = XposedHelpers.getStaticBooleanField(
                    "com.miui.home.launcher.DeviceConfig".findClass(), "sIsImmersiveNavigationBar"
                )
                if (sIsImmersiveNavigationBar) it.result =
                    (height + 8 * context.resources.displayMetrics.density).roundToInt()
            }
        }
    }
}