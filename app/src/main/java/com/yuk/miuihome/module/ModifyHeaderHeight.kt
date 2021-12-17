package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ResourcesHookData
import com.yuk.miuihome.utils.ktx.dp2px

class ModifyHeaderHeight {

    fun init() {
        val value = OwnSP.ownSP.getFloat("recents_task_view_header_height", -1f)
        if (value == -1f || value == 40f) return
        ResourcesHook.hookMap["recents_task_view_header_height"] = ResourcesHookData("dimen", dp2px(value))
    }
}