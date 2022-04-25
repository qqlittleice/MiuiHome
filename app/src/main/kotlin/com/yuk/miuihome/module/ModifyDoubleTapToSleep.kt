package com.yuk.miuihome.module

import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.OwnSP
import de.robv.android.xposed.XposedHelpers

class ModifyDoubleTapToSleep {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("doubleTap", false)) return
        hookAllConstructorAfter("com.miui.home.launcher.Workspace") {
            var mDoubleTapControllerEx = XposedHelpers.getAdditionalInstanceField(it.thisObject, "mDoubleTapControllerEx")
            if (mDoubleTapControllerEx != null) return@hookAllConstructorAfter
            mDoubleTapControllerEx = DoubleTapController((it.args[0] as Context))
            XposedHelpers.setAdditionalInstanceField(it.thisObject, "mDoubleTapControllerEx", mDoubleTapControllerEx)
        }
        findMethod("com.miui.home.launcher.Workspace") {
            name == "dispatchTouchEvent" && parameterTypes[0] == MotionEvent::class.java
        }.hookBefore {
            val mDoubleTapControllerEx = XposedHelpers.getAdditionalInstanceField(it.thisObject, "mDoubleTapControllerEx") as DoubleTapController
            if (!mDoubleTapControllerEx.isDoubleTapEvent(it.args[0] as MotionEvent)) return@hookBefore
            val mCurrentScreenIndex = it.thisObject.getObject("mCurrentScreenIndex")
            val cellLayout = it.thisObject.invokeMethodAuto("getCellLayout", mCurrentScreenIndex)
            if (cellLayout != null) if (cellLayout.invokeMethodAuto("lastDownOnOccupiedCell") as Boolean) return@hookBefore
            if (it.thisObject.invokeMethodAuto("isInNormalEditingMode") as Boolean) return@hookBefore
            val context = it.thisObject.invokeMethodAuto("getContext") as Context
            context.sendBroadcast(Intent("com.miui.app.ExtraStatusBarManager.action_TRIGGER_TOGGLE").putExtra("com.miui.app.ExtraStatusBarManager.extra_TOGGLE_ID", 10))
        }
    }
}