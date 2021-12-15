package com.yuk.miuihome.module

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.View
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

class ModifyAppReturnBlur {

    fun init() {
        if (OwnSP.ownSP.getBoolean("appReturnAmin", false)) {
            val value = (OwnSP.ownSP.getFloat("animationLevel", -1f) * 500).toLong()
            val handler = Handler(Looper.getMainLooper())
            val blurClass = "com.miui.home.launcher.common.BlurUtils".findClass()
            val launcherClass = "com.miui.home.launcher.Launcher".findClass()
            val navStubViewClass = "com.miui.home.recents.NavStubView".findClass()
            lateinit var activity: Activity
            launcherClass.hookAfterMethod("onResume") {
                activity = it.thisObject as Activity
                val view: View = activity.findViewById(activity.resources.getIdentifier("recents_container", "id", Config.hookPackage))
                val runnable = Runnable { blurClass.callStaticMethod("fastBlur", 0.0f, activity.window, true, value) }
                val isFolderShowing = activity.callMethod("isFolderShowing") as Boolean
                val isInEditing = activity.callMethod("isInEditing") as Boolean
                if ((view.visibility == View.GONE) && !isFolderShowing && !isInEditing
                ) {
                    XposedBridge.log("$isFolderShowing")
                    navStubViewClass.hookAfterMethod("startAppToHomeAnim"
                    ) {
                        launcherClass.callStaticMethod("getLauncher", handler.postDelayed(runnable,0))
                    }
                }
            }
        }
    }
}