package com.yuk.miuihome.module

import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.*
import de.robv.android.xposed.XC_MethodHook
import java.util.*

class AlwaysShowMIUIWidget {

    fun init() {
        if (ownSP.getBoolean("alwaysShowMIUIWidget", false)) {
            var hook1: XC_MethodHook.Unhook? = null
            var hook2: XC_MethodHook.Unhook? = null

            "com.miui.home.launcher.widget.WidgetsVerticalAdapter".hookBeforeMethod(
                "buildAppWidgetsItems",
                List::class.java,
                ArrayList::class.java
            ) {
                hook1 = "com.miui.home.launcher.LauncherAppWidgetProviderInfo".hookAfterMethod(
                    "fromProviderInfo",
                    Context::class.java,
                    AppWidgetProviderInfo::class.java
                ) {
                    it.thisObject.apply {
                        setBooleanField("isMIUIWidget", false)
                        getObjectField("providerInfo")?.setIntField("widgetFeatures", 0)
                    }
                }
                hook2 =
                    "com.miui.home.launcher.MIUIWidgetUtil".hookBeforeMethod("isMIUIWidgetSupport") {
                        it.result = false
                    }
            }

            "com.miui.home.launcher.widget.WidgetsVerticalAdapter".hookAfterMethod(
                "buildAppWidgetsItems",
                List::class.java,
                ArrayList::class.java
            ) {
                hook1?.unhook()
                hook2?.unhook()
            }
        }
    }
}