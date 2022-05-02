package com.yuk.miuihome.module

import android.content.ComponentName
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*
import de.robv.android.xposed.XC_MethodHook

class AlwaysShowMIUIWidget {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("alwaysShowMIUIWidget", false)) return
        var hook1: XC_MethodHook.Unhook? = null
        var hook2: XC_MethodHook.Unhook? = null
        "com.miui.home.launcher.widget.WidgetsVerticalAdapter".hookBeforeMethod("buildAppWidgetsItems", List::class.java, ArrayList::class.java
        ) {
            hook1 = "com.miui.home.launcher.widget.MIUIAppWidgetInfo".hookAfterMethod("initMiuiAttribute", ComponentName::class.java
            ) {
                it.thisObject.apply {
                    setBooleanField("isMIUIWidget", false)
                }
            }
            hook2 = "com.miui.home.launcher.MIUIWidgetUtil".hookBeforeMethod("isMIUIWidgetSupport"
            ) {
                    it.result = false
                }
        }
        "com.miui.home.launcher.widget.WidgetsVerticalAdapter".hookAfterMethod("buildAppWidgetsItems", List::class.java, ArrayList::class.java
        ) {
            hook1?.unhook()
            hook2?.unhook()
        }
    }
}