package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.init.InitFields
import com.yuk.miuihome.utils.OwnSP
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

class EnableDockIconShadow {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("isEnableIconShadow", false)) return
        XposedHelpers.findAndHookMethod( // TODO
            "com.miui.home.launcher.Launcher",
            InitFields.ezXClassLoader,
            "isEnableIconShadow",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    param.result = true
                }
            })
    }
}