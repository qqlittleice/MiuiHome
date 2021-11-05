package com.yuk.miuihome.module

import android.view.View
import android.view.ViewGroup
import com.yuk.miuihome.utils.OwnSP.ownSP
import de.robv.android.xposed.XposedHelpers
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class ModifyHideSeekPoints {

    fun init() {
        "com.miui.home.launcher.ScreenView".hookBeforeMethod(
            "updateSeekPoints",
            Int::class.javaPrimitiveType
        ) {
            showSeekBar(it.thisObject as View)
        }
        "com.miui.home.launcher.ScreenView".hookBeforeMethod(
            "addView",
            View::class.java,
            Int::class.javaPrimitiveType,
            ViewGroup.LayoutParams::class.java
        ) {
            showSeekBar(it.thisObject as View)
        }
        "com.miui.home.launcher.ScreenView".hookBeforeMethod(
            "removeScreen",
            Int::class.javaPrimitiveType
        ) {
            showSeekBar(it.thisObject as View)
        }
        "com.miui.home.launcher.ScreenView".hookBeforeMethod(
            "removeScreensInLayout",
            Int::class.javaPrimitiveType,
            Int::class.javaPrimitiveType
        ) {
            showSeekBar(it.thisObject as View)
        }
    }

    private fun showSeekBar(workspace: View) {
        if ("Workspace" != workspace.javaClass.simpleName) return
        val isInEditingMode =
            XposedHelpers.callMethod(workspace, "isInNormalEditingMode") as Boolean
        val mScreenSeekBar = XposedHelpers.getObjectField(workspace, "mScreenSeekBar") as View
        mScreenSeekBar.animate().cancel()
        if (!isInEditingMode && ownSP.getBoolean("hideSeekPoints", false)) {
            mScreenSeekBar.alpha = 0.0f
            mScreenSeekBar.visibility = View.GONE
            return
        }
    }
}