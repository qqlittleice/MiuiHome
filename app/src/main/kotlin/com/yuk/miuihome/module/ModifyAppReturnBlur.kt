package com.yuk.miuihome.module

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.callMethod
import com.yuk.miuihome.utils.ktx.callStaticMethod
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookAfterMethod

class ModifyAppReturnBlur {

    @SuppressLint("DiscouragedApi")
    fun init() {
        if (!OwnSP.ownSP.getBoolean("appReturnAmin", false)) return
        val value = (OwnSP.ownSP.getFloat("appReturnAminSpend", 5f) * 100).toLong()
        val handler = Handler(Looper.getMainLooper())
        val blurClass = "com.miui.home.launcher.common.BlurUtils".findClass()
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
        val isUserBlurWhenOpenFolder = OwnSP.ownSP.getBoolean("blurWhenOpenFolder", false)
        launcherClass.hookAfterMethod("onCreate", Bundle::class.java) {
            val activity = it.thisObject as Activity
            val view: View = activity.findViewById(activity.resources.getIdentifier("recents_container", "id", Config.hostPackage))
            val runnable = Runnable { blurClass.callStaticMethod("fastBlur", 0.0f, activity.window, true, value) }
            var isFolderShowing: Boolean
            var isInEditing: Boolean
            blurClass.hookAfterMethod("fastBlurWhenStartOpenOrCloseApp", Boolean::class.java, launcherClass) { hookParam ->
                val z = hookParam.args[0] as Boolean
                if (view.visibility == View.GONE && !z) {
                    if (isUserBlurWhenOpenFolder) {
                        isFolderShowing = activity.callMethod("isFolderShowing") as Boolean
                        isInEditing = activity.callMethod("isInEditing") as Boolean
                        if (!isFolderShowing) {
                            hookParam.result = blurClass.callStaticMethod("fastBlur", 0.0f, activity.window, true, value + 100L)
                        }
                    } else {
                        hookParam.result = blurClass.callStaticMethod("fastBlur", 0.0f, activity.window, true, value + 100L)
                    }
                }
            }
            launcherClass.hookAfterMethod("onGesturePerformAppToHome") {
                isInEditing = activity.callMethod("isInEditing") as Boolean
                if (view.visibility == View.GONE && !isInEditing) {
                    if (isUserBlurWhenOpenFolder) {
                        isFolderShowing = activity.callMethod("isFolderShowing") as Boolean
                        if (!isFolderShowing) {
                            blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                            handler.postDelayed(runnable, 100)
                        }
                    } else {
                        blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                        handler.postDelayed(runnable, 100)
                    }
                }
            }
        }
    }
}