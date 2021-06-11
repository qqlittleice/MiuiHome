package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.utils.ktx.setReturnConstant

class DisableLog {

    fun init() {
        "com.miui.home.launcher.MiuiHomeLog".setReturnConstant("log", String::class.java, String::class.java, result = null)
    }

}
