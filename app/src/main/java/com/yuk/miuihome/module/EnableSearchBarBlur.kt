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
            if (Config.AndroidSDK < 31) {
                "com.miui.home.launcher.SearchBarStyleData".hookBeforeMethod("isUserBlur"
                ) {
                    it.result = true
                }
            } else if (!OwnSP.ownSP.getBoolean("dockSettings", false) && Config.AndroidSDK == 31) {
                val launcherClass = "com.miui.home.launcher.Launcher".findClass()
                launcherClass.hookAfterMethod("onCreate", Bundle::class.java
                ) {
                    val searchBarObject = it.thisObject.callMethod("getSearchBar") as FrameLayout
                    val blur = BlurFrameLayout(searchBarObject.context)
                    blur.setBlurRadius(100)
                    blur.setCornerRadius(100f)
                    blur.setColor(Color.parseColor("#33626262"))
                    searchBarObject.addView(blur, 0)
                }
            }
        } else "com.miui.home.launcher.SearchBarStyleData".hookBeforeMethod("isUserBlur"
        ) {
            it.result = false
        }
    }
}