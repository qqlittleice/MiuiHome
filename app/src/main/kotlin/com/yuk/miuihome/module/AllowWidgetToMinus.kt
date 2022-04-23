package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.callMethod
import com.yuk.miuihome.utils.ktx.getObjectField
import com.yuk.miuihome.utils.ktx.hookBeforeMethod
import com.yuk.miuihome.utils.ktx.setBooleanField

class AllowWidgetToMinus {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("widgetToMinus", false)) return
        "com.miui.home.launcher.Workspace".hookBeforeMethod("canDragToPa") {
            val currentDragObject = it.thisObject.getObjectField("mDragController")?.callMethod("getCurrentDragObject")
            val dragInfo = currentDragObject?.callMethod("getDragInfo")
            dragInfo?.setBooleanField("isMIUIWidget", true)
        }
    }
}