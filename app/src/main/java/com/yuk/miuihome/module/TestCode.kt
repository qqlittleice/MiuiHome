package com.yuk.miuihome.module

import android.content.Context
import android.view.View
import com.yuk.miuihome.utils.ktx.callMethod
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import java.util.function.Predicate

class TestCode {
    fun init() {
        val widgetInfo = "com.miui.home.launcher.LauncherAppWidgetInfo".findClass()
        val widgetProviderInfo = "android.appwidget.AppWidgetProviderInfo".findClass()
        "com.miui.home.launcher.LauncherAppWidgetHost".hookAfterMethod("createLauncherWidgetView", Context::class.java, Int::class.java, widgetInfo, widgetProviderInfo) {
            val view = it.result as Object
            view.callMethod("getTitleView")?.callMethod("setVisibility", View.GONE)
        }
        val maMlWidgetInfo = "com.miui.home.launcher.maml.MaMlWidgetInfo".findClass()
        "com.miui.home.launcher.Launcher".hookAfterMethod("addMaMl", maMlWidgetInfo, Boolean::class.java, Predicate::class.java) {
            val view = it.result as Object
            view.callMethod("getTitleView")?.callMethod("setVisibility", View.GONE)
        }
    }
}