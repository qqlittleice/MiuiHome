package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findAllMethods
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.yuk.miuihome.utils.OwnSP

class ModifyBlurRadius {

    fun init() {
        val value = OwnSP.ownSP.getFloat("blurRadius", -1f)
        if (value == -1f || value == 1f) return
        if (OwnSP.ownSP.getBoolean("appReturnAmin", false)) return
        val blurUtilsClass = loadClass("com.miui.home.launcher.common.BlurUtils")
        findAllMethods(blurUtilsClass) {
            name == "fastBlur"
        }.hookBefore {
            it.args[0] = it.args[0] as Float * value
        }
    }
}