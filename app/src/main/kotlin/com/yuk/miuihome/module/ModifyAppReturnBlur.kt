package com.yuk.miuihome.module

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.BuildConfig
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import de.robv.android.xposed.XposedBridge

class ModifyAppReturnBlur {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("appReturnAmin", false)) return
        val value = (OwnSP.ownSP.getFloat("animationLevel", -1f) * 480).toLong()
        val handler = Handler(Looper.getMainLooper())
        val blurClass = loadClass("com.miui.home.launcher.common.BlurUtils")
        val launcherClass =  loadClass("com.miui.home.launcher.Launcher")
        val navStubViewClass =  loadClass("com.miui.home.recents.NavStubView")
        findMethod(launcherClass) {
            name == "onCreate" && parameterTypes[0] == Bundle::class.java //TODO
        }.hookAfter {
            val activity = it.thisObject as Activity
            val view: View = activity.findViewById(activity.resources.getIdentifier("recents_container", "id", Config.hostPackage))
            val runnable = Runnable { blurClass.invokeStaticMethodAuto("fastBlur", 0.0f, activity.window, true, value) }
            findMethod(navStubViewClass) {
                name == "performAppToHome"
            }.hookAfter {
                val isFolderShowing = activity.invokeMethodAuto("isFolderShowing") as Boolean
                val isInEditing = activity.invokeMethodAuto("isInEditing") as Boolean
                if (BuildConfig.DEBUG) {
                    XposedBridge.log("MiuiHome: callMethod [isFolderShowing] succeeded, now it's $isFolderShowing")
                    XposedBridge.log("MiuiHome: callMethod [isInEditing] succeeded, now it's $isInEditing")
                }
                if (XposedInit().checkIsAlpha()) {
                    val isUserBlurWhenOpenFolder = blurClass.invokeStaticMethodAuto("isUserBlurWhenOpenFolder") as Boolean
                    if (BuildConfig.DEBUG) {
                        XposedBridge.log("MiuiHome: callMethod [isUserBlurWhenOpenFolder] succeeded, now it's $isUserBlurWhenOpenFolder")
                    }
                    if (view.visibility == View.GONE && !isInEditing)
                        if ((isUserBlurWhenOpenFolder && !isFolderShowing) or (!isUserBlurWhenOpenFolder && isFolderShowing))
                            handler.postDelayed(runnable, 100)
                } else if (!XposedInit().checkIsAlpha() && OwnSP.ownSP.getBoolean("blurWhenOpenFolder", false)) {
                    if  (view.visibility == View.GONE && !isInEditing  && !isFolderShowing)
                        handler.postDelayed(runnable, 100)
                } else if (!XposedInit().checkIsAlpha() && !OwnSP.ownSP.getBoolean("blurWhenOpenFolder", false)){
                    if (view.visibility == View.GONE && !isInEditing)
                        handler.postDelayed(runnable, 100)
                }
            }
        }
    }
}