package com.yuk.miuihome.module

import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*
import com.zhenxiang.blur.WindowBlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius

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
                    val blur = WindowBlurFrameLayout(searchBarObject.context)
                    blur.blurController.apply {
                        backgroundColour = Color.parseColor("#33626262")
                        cornerRadius = CornersRadius.all(100f)
                    }
                    searchBarObject.addView(blur, 0)
                }
            }
        } else "com.miui.home.launcher.SearchBarStyleData".hookBeforeMethod("isUserBlur"
        ) {
            it.result = false
        }
    }
}