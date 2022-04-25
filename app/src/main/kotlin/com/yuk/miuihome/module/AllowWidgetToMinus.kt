package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.OwnSP

class AllowWidgetToMinus {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("widgetToMinus", false)) return
        findMethod("com.miui.home.launcher.Workspace") {
            name == "canDragToPa"
        }.hookBefore {
            val currentDragObject = it.thisObject.getObjectOrNull("mDragController")?.invokeMethodAuto("getCurrentDragObject")
            val dragInfo = currentDragObject?.invokeMethodAuto("getDragInfo")
            dragInfo?.putObject("isMIUIWidget", true)
        }
    }
}