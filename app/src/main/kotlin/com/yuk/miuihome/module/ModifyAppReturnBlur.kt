package com.yuk.miuihome.module

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
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
        val value = (OwnSP.ownSP.getFloat("appReturnAminSpend", 2f) * 100).toLong()
        val blurClass = "com.miui.home.launcher.common.BlurUtils".findClass()
        val launcherClass = "com.miui.home.launcher.Launcher".findClass()
        val isUserBlurWhenOpenFolder = OwnSP.ownSP.getBoolean("blurWhenOpenFolder", false)
        launcherClass.hookAfterMethod("onResume") {
            val activity = it.thisObject as Activity
            val view: View = activity.findViewById(activity.resources.getIdentifier("recents_container", "id", Config.hostPackage))
            var isFolderShowing: Boolean
            var isInEditing: Boolean
            blurClass.hookAfterMethod("fastBlurWhenStartOpenOrCloseApp", Boolean::class.java, launcherClass) { hookParam ->
                val z = hookParam.args[0] as Boolean
                isInEditing = activity.callMethod("isInEditing") as Boolean
                if (view.visibility == View.GONE && !isInEditing && !z) {
                    if (isUserBlurWhenOpenFolder) {
                        isFolderShowing = activity.callMethod("isFolderShowing") as Boolean
                        if (!isFolderShowing) {
                            hookParam.result = blurClass.callStaticMethod("fastBlur", 0.0f, activity.window, true, value)
                        }
                    } else {
                        hookParam.result = blurClass.callStaticMethod("fastBlur", 0.0f, activity.window, true, value)
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
                            blurClass.callStaticMethod("fastBlur", 0.0f, activity.window, true, value)
                        }
                    } else {
                        blurClass.callStaticMethod("fastBlur", 1.0f, activity.window, true, 0L)
                        blurClass.callStaticMethod("fastBlur", 0.0f, activity.window, true, value)
                    }
                }
            }
        }
    }
}