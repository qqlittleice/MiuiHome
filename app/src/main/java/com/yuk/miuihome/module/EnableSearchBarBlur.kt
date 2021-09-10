package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class EnableSearchBarBlur {

    fun init() {
        if (OwnSP.ownSP.getBoolean("searchBarBlur", false)) {
            "com.miui.home.launcher.SearchBarStyleData".setReturnConstant(
                "isUserBlur",
                result = true
            )
        }
    }
}