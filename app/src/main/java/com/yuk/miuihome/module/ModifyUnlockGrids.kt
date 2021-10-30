package com.yuk.miuihome.module

import android.content.Context
import com.yuk.miuihome.ResourcesHook
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ResourcesHookData
import com.yuk.miuihome.utils.ktx.setReturnConstant

class ModifyUnlockGrids {

    fun init() {
        if (ownSP.getBoolean("unlockGrids", false)) {
            ResourcesHook.hookMap["config_cell_count_x"] = ResourcesHookData("integer", 3)
            ResourcesHook.hookMap["config_cell_count_y"] = ResourcesHookData("integer", 4)
            ResourcesHook.hookMap["config_cell_count_x_min"] = ResourcesHookData("integer", 3)
            ResourcesHook.hookMap["config_cell_count_y_min"] = ResourcesHookData("integer", 4)
            ResourcesHook.hookMap["config_cell_count_x_max"] = ResourcesHookData("integer", 10)
            ResourcesHook.hookMap["config_cell_count_y_max"] = ResourcesHookData("integer", 10)

            "com.miui.home.launcher.compat.LauncherCellCountCompatDevice".setReturnConstant(
                "shouldUseDeviceValue",
                Context::class.java,
                Int::class.java,
                result = false
            )
        }
    }
}