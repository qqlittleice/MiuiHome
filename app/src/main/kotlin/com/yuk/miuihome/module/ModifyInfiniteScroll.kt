package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObject
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.invokeMethodAuto
import com.yuk.miuihome.utils.OwnSP

class ModifyInfiniteScroll {

    fun init() {
        if (OwnSP.ownSP.getBoolean("infiniteScroll", false)) {
            findMethod("com.miui.home.launcher.ScreenView") {
                name == "getSnapToScreenIndex" && parameterTypes[0] == Int::class.javaPrimitiveType && parameterTypes[1] == Int::class.javaPrimitiveType && parameterTypes[2] == Int::class.javaPrimitiveType
            }.hookAfter {
                if (it.args[0] !== it.result) return@hookAfter
                val screenCount = it.thisObject.invokeMethodAuto("getScreenCount") as Int
                if (it.args[2] as Int == -1 && it.args[0] as Int == 0) it.result = screenCount
                else if (it.args[2] as Int == 1 && it.args[0] as Int == screenCount - 1) it.result = 0
            }
            findMethod("com.miui.home.launcher.ScreenView") {
                name == "getSnapUnitIndex" && parameterTypes[0] == Int::class.javaPrimitiveType
            }.hookAfter {
                val mCurrentScreenIndex = it.thisObject.getObject("mCurrentScreenIndex")
                if (mCurrentScreenIndex != it.result as Int) return@hookAfter
                val screenCount = it.thisObject.invokeMethodAuto("getScreenCount") as Int
                if (it.result as Int == 0) it.result = screenCount
                else if (it.result as Int == screenCount - 1) it.result = 0
            }
        }
    }
}