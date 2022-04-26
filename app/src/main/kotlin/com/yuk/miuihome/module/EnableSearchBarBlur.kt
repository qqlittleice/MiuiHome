package com.yuk.miuihome.module

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import com.github.kyuubiran.ezxhelper.init.InitFields
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.OwnSP
import com.zhenxiang.blur.WindowBlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

class EnableSearchBarBlur {

    fun init() {
        if (OwnSP.ownSP.getBoolean("searchBarBlur", false)) {
            if (Build.VERSION.SDK_INT < 31) {
                findMethod("com.miui.home.launcher.SearchBarStyleData") {
                    name == "isUserBlur"
                }.hookReturnConstant(true)
            } else if (!OwnSP.ownSP.getBoolean("dockSettings", false) && Build.VERSION.SDK_INT == 31) {
                XposedHelpers.findAndHookMethod( // TODO
                    "com.miui.home.launcher.Launcher",
                    InitFields.ezXClassLoader,
                    "onCreate",
                    Bundle::class.java,
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            val searchBarObject = param.thisObject.invokeMethodAuto("getSearchBar") as FrameLayout
                            val blur = WindowBlurFrameLayout(searchBarObject.context)
                            blur.blurController.apply {
                                backgroundColour = Color.parseColor("#33626262")
                                cornerRadius = CornersRadius.all(100f)
                            }
                            searchBarObject.addView(blur, 0)
                        }
                    })
            }
        } else
            findMethod("com.miui.home.launcher.SearchBarStyleData") {
                name == "isUserBlur"
            }.hookReturnConstant(false)
    }
}