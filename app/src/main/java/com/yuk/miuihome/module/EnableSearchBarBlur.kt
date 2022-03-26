package com.yuk.miuihome.module

import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*
import com.yuk.miuihome.view.BlurFrameLayout

class EnableSearchBarBlur {

    fun init() {
        if (OwnSP.ownSP.getBoolean("searchBarBlur", false)) {
            "com.miui.home.launcher.SearchBarStyleData".hookBeforeMethod("isUserBlur") {
                it.result = true
            }
            "com.miui.home.launcher.Launcher".hookAfterMethod("onCreate", Bundle::class.java
            ) {
                // 添加模糊
                if (OwnSP.ownSP.getBoolean("searchBarBlur", false) && Config.AndroidSDK == 31) {
                    val searchBarObject = it.thisObject.callMethod("getSearchBar") as FrameLayout
                    val blur = BlurFrameLayout(searchBarObject.context)
                    blur.setBlurRadius(100)
                    blur.setCornerRadius(dp2px((OwnSP.ownSP.getFloat("dockRadius", 2.5f) * 10)).toFloat())
                    blur.setColor(Color.parseColor("#33626262"))
                    searchBarObject.addView(blur, 0)
                }
            }
        }
        else
            "com.miui.home.launcher.SearchBarStyleData".hookBeforeMethod("isUserBlur") {
                it.result = false
            }
    }
}