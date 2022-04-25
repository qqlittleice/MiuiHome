package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.yuk.miuihome.utils.OwnSP

class ModifyAnimDurationRatio {

    fun init() {
        val value = OwnSP.ownSP.getFloat("animationLevel", -1f)
        if (value == -1f) return
        findMethod("com.miui.home.recents.util.RectFSpringAnim") {
            name == "getModifyResponse" && parameterTypes[0] == Float::class.java
        }.hookBefore {
            it.result = it.args[0] as Float * value
        }
    }
}