package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class ModifyAnimDurationRatio {

    fun init() {
        val value = OwnSP.ownSP.getFloat("animationLevel", -1f)
        if (value == -1f) return
        "com.miui.home.recents.util.RectFSpringAnim".hookBeforeMethod("getModifyResponse", Float::class.java) {
            it.result = it.args[0] as Float * value
        }
    }
}