package com.yuk.miuihome.module

import android.view.View
import android.view.ViewGroup
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.OwnSP

class ModifyHideSeekPoints {

    fun init() {
        val screenViewClass = loadClass("com.miui.home.launcher.ScreenView")
        findMethod(screenViewClass) {
            name == "updateSeekPoints" && parameterTypes[0] == Int::class.javaPrimitiveType
        }.hookBefore {
            showSeekBar(it.thisObject as View)
        }
        findMethod(screenViewClass) {
            name == "addView" && parameterTypes[0] == View::class.java && parameterTypes[1] == Int::class.javaPrimitiveType && parameterTypes[2] == ViewGroup.LayoutParams::class.java
        }.hookBefore {
            showSeekBar(it.thisObject as View)
        }
        findMethod(screenViewClass) {
            name == "removeScreen" && parameterTypes[0] == Int::class.javaPrimitiveType
        }.hookBefore {
            showSeekBar(it.thisObject as View)
        }
        findMethod(screenViewClass) {
            name == "removeScreensInLayout" && parameterTypes[0] == Int::class.javaPrimitiveType && parameterTypes[1] == Int::class.javaPrimitiveType
        }.hookBefore {
            showSeekBar(it.thisObject as View)
        }
    }

    private fun showSeekBar(workspace: View) {
        if ("Workspace" != workspace.javaClass.simpleName) return
        val isInEditingMode = workspace.invokeMethodAuto("isInNormalEditingMode") as Boolean
        val mScreenSeekBar = workspace.getObject("mScreenSeekBar") as View
        mScreenSeekBar.animate().cancel()
        if (!isInEditingMode && OwnSP.ownSP.getBoolean("hideSeekPoints", false)) {
            mScreenSeekBar.alpha = 0.0f
            mScreenSeekBar.visibility = View.GONE
            return
        }
    }
}