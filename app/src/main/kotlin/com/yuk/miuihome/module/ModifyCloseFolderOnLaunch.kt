package com.yuk.miuihome.module

import android.view.View
import com.github.kyuubiran.ezxhelper.init.InitFields
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.OwnSP
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

class ModifyCloseFolderOnLaunch {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("closeFolder", false)) return
        XposedHelpers.findAndHookMethod( // TODO
            "com.miui.home.launcher.Launcher",
            InitFields.ezXClassLoader,
            "launch",
            View::class.java,
            loadClass("com.miui.home.launcher.ShortcutInfo"),
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val mHasLaunchedAppFromFolder = param.thisObject.getObjectAs<Boolean>("mHasLaunchedAppFromFolder")
                    if (mHasLaunchedAppFromFolder) param.thisObject.invokeMethodAuto("closeFolder")
                }
            })
    }
}
