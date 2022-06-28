package com.yuk.miuihome.module

import android.content.ComponentName
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookMethod
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import com.yuk.miuihome.utils.ktx.hookBeforeMethod
import com.yuk.miuihome.utils.ktx.setBooleanField
import de.robv.android.xposed.XC_MethodHook

class AlwaysShowMIUIWidget {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("alwaysShowMIUIWidget", false)) return
        var hook1: XC_MethodHook.Unhook? = null
        var hook2: XC_MethodHook.Unhook? = null
        try {
            findMethod("com.miui.home.launcher.widget.WidgetsVerticalAdapter") {
                name == "buildAppWidgetsItems"
            }
        } catch (e: Exception) {
            findMethod("com.miui.home.launcher.widget.BaseWidgetsVerticalAdapter") {
                name == "buildAppWidgetsItems"
            }
        }.hookMethod {
            before {
                hook1 = "com.miui.home.launcher.widget.MIUIAppWidgetInfo".hookAfterMethod(
                    "initMiuiAttribute", ComponentName::class.java
                ) {
                    it.thisObject.setBooleanField("isMIUIWidget", false)
                }
                hook2 = "com.miui.home.launcher.MIUIWidgetUtil".hookBeforeMethod(
                    "isMIUIWidgetSupport"
                ) {
                    it.result = false
                }
            }
            after {
                hook1?.unhook()
                hook2?.unhook()
            }
        }
    }
}