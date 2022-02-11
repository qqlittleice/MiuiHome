package com.yuk.miuihome.module

import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.setReturnConstant

class EnableSearchBarBlur {

    fun init() {
        if (OwnSP.ownSP.getBoolean("searchBarBlur", false))
            "com.miui.home.launcher.SearchBarStyleData".setReturnConstant("isUserBlur", result = true)
        else
            "com.miui.home.launcher.SearchBarStyleData".setReturnConstant("isUserBlur", result = false)
    }
}