package com.yuk.miuihome.module

import android.os.Handler
import android.os.Looper
import android.view.View
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.HomeContext
import com.yuk.miuihome.utils.ktx.*

class AppReturnAmin {
    fun init() {
        val a = Handler(Looper.getMainLooper())
        val blurClass = "com.miui.home.launcher.common.BlurUtils".findClass()
        val view: View = HomeContext.activity.findViewById(HomeContext.context.resources.getIdentifier("recents_container", "id", Config.hookPackage))
        val before = blurClass.callStaticMethod("fastBlur", 1f, HomeContext.activity.window, true, 0L)
        val run = blurClass.callStaticMethod("fastBlur", 0f, HomeContext.activity.window, true, 800L)
        if ((view.visibility == View.GONE)
            && !(HomeContext.activity.callMethod("isInEditing", *arrayOfNulls(0)) as Boolean)
            && !(HomeContext.activity.callMethod("isFolderShowing", *arrayOfNulls(0)) as Boolean)
        ) {
            "com.miui.home.launcher.Launcher".hookBeforeMethod(
                "onResume"
            ) {

            }
            "com.miui.home.recents.NavStubView".hookAfterMethod(
                "startAppToHomeAnim"
            ) {

            }
        }
    }
}