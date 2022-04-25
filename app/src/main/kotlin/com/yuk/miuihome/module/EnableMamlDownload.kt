package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.yuk.miuihome.utils.OwnSP

class EnableMamlDownload {

    fun init() {
        if (OwnSP.ownSP.getBoolean("mamlDownload", false))
            findMethod("com.miui.home.launcher.common.CpuLevelUtils") {
                name == "needMamlDownload"
            }.hookReturnConstant(true)
        else
            findMethod("com.miui.home.launcher.common.CpuLevelUtils") {
                name == "needMamlDownload"
            }.hookReturnConstant(false)
    }
}