package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookBeforeAllMethods

class ModifyBlurRadius {

    fun init() {
        val value = OwnSP.ownSP.getFloat("blurRadius", -1f)
        if (value == -1f || value == 1f) return
        val blurUtilsClass = "com.miui.home.launcher.common.BlurUtils".findClass()
        blurUtilsClass.hookBeforeAllMethods("fastBlur") {
            it.args[0] = it.args[0] as Float * value
        }
    }
}