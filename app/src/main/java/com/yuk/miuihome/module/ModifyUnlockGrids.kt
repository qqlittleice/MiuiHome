package com.yuk.miuihome.module

import com.yuk.miuihome.ResourcesHook
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ResourcesHookData
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookAllMethods
import de.robv.android.xposed.XC_MethodReplacement

class ModifyUnlockGrids {

    fun init() {
        if (ownSP.getBoolean("unlockGrids", false)) {
            "com.miui.home.launcher.compat.LauncherCellCountCompatDevice".findClass()
                .hookAllMethods(
                    "shouldUseDeviceValue",
                    XC_MethodReplacement.returnConstant(false)
                )
            ResourcesHook.hookMap["config_cell_count_x"] = ResourcesHookData("integer", 3)
            ResourcesHook.hookMap["config_cell_count_y"] = ResourcesHookData("integer", 4)
            ResourcesHook.hookMap["config_cell_count_x_min"] = ResourcesHookData("integer", 3)
            ResourcesHook.hookMap["config_cell_count_y_min"] = ResourcesHookData("integer", 4)
            ResourcesHook.hookMap["config_cell_count_x_max"] = ResourcesHookData("integer", 10)
            ResourcesHook.hookMap["config_cell_count_y_max"] = ResourcesHookData("integer", 10)
        }
    }
}