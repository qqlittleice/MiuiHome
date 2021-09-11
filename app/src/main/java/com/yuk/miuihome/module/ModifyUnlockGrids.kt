package com.yuk.miuihome.module

import com.yuk.miuihome.ResourcesHook
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ResourcesHookData

class ModifyUnlockGrids {

    fun init() {
        if (OwnSP.ownSP.getBoolean("unlockGrids", false)) {
            ResourcesHook.hookMap["config_cell_count_x"] = ResourcesHookData("integer", 3)
            ResourcesHook.hookMap["config_cell_count_y"] = ResourcesHookData("integer", 4)
            ResourcesHook.hookMap["config_cell_count_x_min"] = ResourcesHookData("integer", 3)
            ResourcesHook.hookMap["config_cell_count_y_min"] = ResourcesHookData("integer", 4)
            ResourcesHook.hookMap["config_cell_count_x_max"] = ResourcesHookData("integer", 9)
            ResourcesHook.hookMap["config_cell_count_y_max"] = ResourcesHookData("integer", 9)
        }
    }
}