package com.yuk.miuihome.module

import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.callMethod
import com.yuk.miuihome.view.utils.ktx.getObjectField
import com.yuk.miuihome.view.utils.ktx.hookBeforeMethod
import com.yuk.miuihome.view.utils.ktx.setBooleanField

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