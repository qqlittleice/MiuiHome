package com.yuk.miuihome.module

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import com.github.kyuubiran.ezxhelper.utils.Log
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import com.zhenxiang.blur.WindowBlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius
import de.robv.android.xposed.XposedHelpers

class ModifyPadA12DockBlur {

    @SuppressLint("DiscouragedApi")
    fun init() {
        if (!OwnSP.ownSP.getBoolean("PadDockBlur", false) || Build.VERSION.SDK_INT < 31) return
        try {
            "com.miui.home.launcher.Launcher".hookAfterMethod("onCreate", Bundle::class.java) {
                val activity = it.thisObject as Activity
                val view = activity.findViewById(activity.resources.getIdentifier("hotseat_background", "id", Config.hostPackage)) as ViewGroup
                val blur = WindowBlurFrameLayout(view.context)
                blur.blurController.apply {
                    backgroundColour = Color.parseColor("#44FFFFFF")
                    cornerRadius = CornersRadius.all(40f)
                }
                view.addView(blur)
            }
        } catch (e:XposedHelpers.ClassNotFoundError) {
            Log.ex(e)
        }
    }
}