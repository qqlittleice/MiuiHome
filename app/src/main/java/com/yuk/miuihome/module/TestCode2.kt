package com.yuk.miuihome.module

import com.yuk.miuihome.ResourcesHook
import com.yuk.miuihome.utils.ResourcesHookData

class TestCode2 {

    fun init() {
        ResourcesHook.hookMap["config_hide_hotseats_app_title"] = ResourcesHookData("bool", false)
    }

}