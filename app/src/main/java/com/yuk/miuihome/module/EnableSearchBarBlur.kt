package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class EnableSearchBarBlur {

    fun init() {
        if (OwnSP.ownSP.getBoolean("searchBarBlur", false))
            "com.miui.home.launcher.SearchBarStyleData".hookBeforeMethod("isUserBlur") {
                it.result = true
            }
        else
            "com.miui.home.launcher.SearchBarStyleData".hookBeforeMethod("isUserBlur") {
                it.result = false
            }
    }
}