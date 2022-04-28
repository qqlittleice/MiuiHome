package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class EnableMamlDownload {

    fun init() {
        if (OwnSP.ownSP.getBoolean("mamlDownload", false))
            "com.miui.home.launcher.common.CpuLevelUtils".hookBeforeMethod("needMamlDownload") {
                it.result = true
            }
        else
            "com.miui.home.launcher.common.CpuLevelUtils".hookBeforeMethod("needMamlDownload") {
                it.result = false
            }
    }
}