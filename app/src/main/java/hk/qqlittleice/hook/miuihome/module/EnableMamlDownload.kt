package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.ktx.setReturnConstant

class EnableMamlDownload {

    fun init() {
        if (OwnSP.ownSP.getBoolean("mamlDownload", false)) {
            "com.miui.home.launcher.common.CpuLevelUtils".setReturnConstant("needMamlDownload", result = true)
        }
    }

}