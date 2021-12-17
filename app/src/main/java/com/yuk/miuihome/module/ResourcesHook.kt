package com.yuk.miuihome.module

import android.content.res.Resources
import com.yuk.miuihome.utils.HomeContext
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ResourcesHookData
import com.yuk.miuihome.utils.ResourcesHookMap
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookBeforeAllMethods
import com.yuk.miuihome.utils.ktx.hookBeforeMethod
import de.robv.android.xposed.XC_MethodHook

class ResourcesHook {

    private fun hook(param: XC_MethodHook.MethodHookParam) {
        try {
            val res = HomeContext.context.resources
            val resName = res.getResourceEntryName(param.args[0] as Int)
            val resType = res.getResourceTypeName(param.args[0] as Int)
            if (hookMap.isKeyExist(resName)) {
                if (hookMap[resName]?.type == resType) {
                    param.result = hookMap[resName]?.afterValue
                    //XposedBridge.log("$resName hooked! after value = ${hookMap[resName]?.afterValue}")
                }
            }
        } catch (ignore: Exception) {
        }
    }

    fun init() {
        Resources::class.java.hookBeforeMethod("getBoolean", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getDimension", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getDimensionPixelOffset", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getDimensionPixelSize", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getInteger", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getText", Int::class.javaPrimitiveType) { hook(it) }

        val value = OwnSP.ownSP.getFloat("recents_task_view_rounded_corners_radius", -1f)
        val value1 = OwnSP.ownSP.getFloat("recents_task_view_header_height", -1f)

        if (OwnSP.ownSP.getBoolean("unlockGrids", false)) {
            val deviceClass = "com.miui.home.launcher.compat.LauncherCellCountCompatDevice".findClass()
            deviceClass.hookBeforeAllMethods("shouldUseDeviceValue") { it.result = false }
            hookMap["config_cell_count_x"] = ResourcesHookData("integer", 3)
            hookMap["config_cell_count_y"] = ResourcesHookData("integer", 4)
            hookMap["config_cell_count_x_min"] = ResourcesHookData("integer", 3)
            hookMap["config_cell_count_y_min"] = ResourcesHookData("integer", 4)
            hookMap["config_cell_count_x_max"] = ResourcesHookData("integer", 16)
            hookMap["config_cell_count_y_max"] = ResourcesHookData("integer", 18)
        }

        if (value != -1f || value != 20f) {
            hookMap["recents_task_view_rounded_corners_radius_min"] = ResourcesHookData("dimen", dp2px(value))
            hookMap["recents_task_view_rounded_corners_radius_max"] = ResourcesHookData("dimen", dp2px(value))
        }

        if (value1 != -1f || value1 != 40f) {
            hookMap["recents_task_view_header_height"] = ResourcesHookData("dimen", dp2px(value1))
        }
    }

    companion object {
        val hookMap = ResourcesHookMap<String, ResourcesHookData>()
    }
}