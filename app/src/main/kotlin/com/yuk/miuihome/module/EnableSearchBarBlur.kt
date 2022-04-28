package com.yuk.miuihome.module

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.OwnSP
import com.zhenxiang.blur.WindowBlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius

class EnableSearchBarBlur {

    fun init() {
        if (OwnSP.ownSP.getBoolean("searchBarBlur", false)) {
            if (Build.VERSION.SDK_INT < 31) {
                findMethod("com.miui.home.launcher.SearchBarStyleData") {
                    name == "isUserBlur"
                }.hookReturnConstant(true)
            } else if (!OwnSP.ownSP.getBoolean("dockSettings", false) && Build.VERSION.SDK_INT == 31) {
                val launcherClass = loadClass("com.miui.home.launcher.Launcher")
                findMethod(launcherClass) {
                    name == "onCreate" && parameterTypes[0] == Bundle::class.java //TODO
                }.hookAfter {
                    val searchBarObject = it.thisObject.invokeMethodAuto("getSearchBar") as FrameLayout
                    val blur = WindowBlurFrameLayout(searchBarObject.context)
                    blur.blurController.apply {
                        backgroundColour = Color.parseColor("#33626262")
                        cornerRadius = CornersRadius.all(100f)
                    }
                    searchBarObject.addView(blur, 0)
                }
            }
        } else
            findMethod("com.miui.home.launcher.SearchBarStyleData") {
                name == "isUserBlur"
            }.hookReturnConstant(false)
    }
}