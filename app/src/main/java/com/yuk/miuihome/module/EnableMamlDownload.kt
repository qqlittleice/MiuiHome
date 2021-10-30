package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.setReturnConstant

class EnableMamlDownload {

    fun init() {
        if (ownSP.getBoolean("mamlDownload", false)) {
            "com.miui.home.launcher.common.CpuLevelUtils".setReturnConstant(
                "needMamlDownload",
                result = true
            )
        } else {
            "com.miui.home.launcher.common.CpuLevelUtils".setReturnConstant(
                "needMamlDownload",
                result = false
            )
        }
    }
}