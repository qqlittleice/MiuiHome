package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class EnableSearchBarBlur {

    fun init() {
        if (ownSP.getBoolean("searchBarBlur", false)) {
            "com.miui.home.launcher.SearchBarStyleData".setReturnConstant(
                "isUserBlur",
                result = true
            )
        } else {
            "com.miui.home.launcher.SearchBarStyleData".setReturnConstant(
                "isUserBlur",
                result = false
            )
        }
    }
}