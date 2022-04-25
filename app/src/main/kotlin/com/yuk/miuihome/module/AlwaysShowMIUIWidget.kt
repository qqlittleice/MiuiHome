package com.yuk.miuihome.module

import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.OwnSP
import de.robv.android.xposed.XC_MethodHook

class AlwaysShowMIUIWidget {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("alwaysShowMIUIWidget", false)) return
        var hook1: XC_MethodHook.Unhook? = null
        var hook2: XC_MethodHook.Unhook? = null
        findMethod("com.miui.home.launcher.widget.WidgetsVerticalAdapter") {
            name == "buildAppWidgetsItems" && parameterTypes[0] == List::class.java && parameterTypes[1] == ArrayList::class.java
        }.hookBefore {
            hook1 = findMethod("com.miui.home.launcher.LauncherAppWidgetProviderInfo") {
                name == "fromProviderInfo" && parameterTypes[0] == Context::class.java && parameterTypes[1] == AppWidgetProviderInfo::class.java
            }.hookAfter {
                it.thisObject.apply {
                    putObject("isMIUIWidget", false)
                    getObjectOrNull("providerInfo")?.putObject("widgetFeatures", 0)
                }
            }
            hook2 = findMethod("com.miui.home.launcher.MIUIWidgetUtil") {
                name == "fromProviderInfo" && parameterTypes[0] == Context::class.java && parameterTypes[1] == AppWidgetProviderInfo::class.java
            }.hookReturnConstant(false)
        }
        findMethod("com.miui.home.launcher.widget.WidgetsVerticalAdapter") {
            name == "buildAppWidgetsItems" && parameterTypes[0] == List::class.java && parameterTypes[1] == ArrayList::class.java
        }.hookAfter {
            hook1?.unhook()
            hook2?.unhook()
        }
    }
}