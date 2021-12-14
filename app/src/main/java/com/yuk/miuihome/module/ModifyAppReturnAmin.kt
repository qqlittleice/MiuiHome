package com.yuk.miuihome.module

import android.view.View
import android.view.Window
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.HomeContext
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*

class ModifyAppReturnAmin {
    fun init() {
        if (OwnSP.ownSP.getBoolean("appReturnAmin", false)) {
            val value = OwnSP.ownSP.getFloat("animationLevel", -1f).toLong()
            val blurClass = "com.miui.home.launcher.common.BlurUtils".findClass()
            val launcherClass = "com.miui.home.launcher.Launcher".findClass()
            val view: View = HomeContext.activity.findViewById(HomeContext.context.resources.getIdentifier("recents_container", "id", Config.hookPackage))
            if ((view.visibility == View.GONE)
                && !(launcherClass.callMethod("isInEditing", 0) as Boolean)
                && !(launcherClass.callMethod("isFolderShowing", 0) as Boolean)
            ) {
                "com.miui.home.recents.NavStubView".hookAfterMethod("startAppToHomeAnim"
                ) {
                    blurClass.callStaticMethod("fastBlur", 0f, Window::class.java, true, value)
                }
                "com.miui.home.launcher.Launcher".hookBeforeMethod("onResume"
                ) {
                    blurClass.callStaticMethod("fastBlur", 1f, Window::class.java, true, 0L)
                }
            }
        }
    }
}