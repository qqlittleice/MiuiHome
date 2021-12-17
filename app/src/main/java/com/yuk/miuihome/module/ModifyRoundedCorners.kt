package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ResourcesHookData
import com.yuk.miuihome.utils.ktx.dp2px

class ModifyRoundedCorners {

    fun init() {
        val value = OwnSP.ownSP.getFloat("recents_task_view_rounded_corners_radius", -1f)
        if (value == -1f || value == 20f) return
        ResourcesHook.hookMap["recents_task_view_rounded_corners_radius_min"] = ResourcesHookData("dimen", dp2px(value))
        ResourcesHook.hookMap["recents_task_view_rounded_corners_radius_max"] = ResourcesHookData("dimen", dp2px(value))
    }
}