package com.yuk.miuihome.module

import android.content.Context
import android.view.View
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.callMethod
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import java.util.function.Predicate

class ModifyHideWidgetTitles {

    fun init() {
        if (ownSP.getBoolean("hideWidgetTitles", false)) {
            val widgetInfo = "com.miui.home.launcher.LauncherAppWidgetInfo".findClass()
            val widgetProviderInfo = "android.appwidget.AppWidgetProviderInfo".findClass()
            val maMlWidgetInfo = "com.miui.home.launcher.maml.MaMlWidgetInfo".findClass()
            "com.miui.home.launcher.LauncherAppWidgetHost".hookAfterMethod(
                "createLauncherWidgetView",
                Context::class.java,
                Int::class.javaPrimitiveType,
                widgetInfo,
                widgetProviderInfo
            ) {
                val view = it.result as Any
                view.callMethod("getTitleView")?.callMethod("setVisibility", View.GONE)
            }
            "com.miui.home.launcher.Launcher".hookAfterMethod(
                "addMaMl",
                maMlWidgetInfo,
                Boolean::class.java,
                Predicate::class.java
            ) {
                val view = it.result as Any
                view.callMethod("getTitleView")?.callMethod("setVisibility", View.GONE)
            }
        }
    }
}