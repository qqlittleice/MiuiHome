package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.ktx.setReturnConstant

class EnableSearchBarBlur {

    fun init() {

        if (OwnSP.ownSP.getBoolean("searchBarBlur", false)) {
            "com.miui.home.launcher.SearchBarStyleData".setReturnConstant("isUserBlur", result = true)
            "com.miui.home.launcher.SearchBarStyleData".setReturnConstant("getBlurStyle", result = null)
        }
    }

}