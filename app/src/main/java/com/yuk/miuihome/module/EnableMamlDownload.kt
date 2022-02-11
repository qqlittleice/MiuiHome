package com.yuk.miuihome.module

import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.setReturnConstant

class EnableMamlDownload {

    fun init() {
        if (OwnSP.ownSP.getBoolean("mamlDownload", false))
            "com.miui.home.launcher.common.CpuLevelUtils".setReturnConstant("needMamlDownload", result = true)
        else
            "com.miui.home.launcher.common.CpuLevelUtils".setReturnConstant("needMamlDownload", result = false)
    }
}