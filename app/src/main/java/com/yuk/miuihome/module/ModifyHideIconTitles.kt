package com.yuk.miuihome.module

import com.yuk.miuihome.ResourcesHook
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ResourcesHookData

class ModifyHideIconTitles {

    fun init() {
        if (OwnSP.ownSP.getBoolean("hideIconTitles", false)) {
            ResourcesHook.hookMap["workspace_icon_text_size"] = ResourcesHookData("dimen", 0.0f)
        }
    }
}
