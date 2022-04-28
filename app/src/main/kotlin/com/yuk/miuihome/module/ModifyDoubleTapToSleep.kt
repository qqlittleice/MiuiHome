package com.yuk.miuihome.module

import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*
import de.robv.android.xposed.XposedHelpers

class ModifyDoubleTapToSleep {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("doubleTap", false)) return
        val workspace = "com.miui.home.launcher.Workspace".findClass()
        workspace.hookAfterAllConstructors {
            var mDoubleTapControllerEx = XposedHelpers.getAdditionalInstanceField(it.thisObject, "mDoubleTapControllerEx")
            if (mDoubleTapControllerEx != null) return@hookAfterAllConstructors
            mDoubleTapControllerEx = DoubleTapController((it.args[0] as Context))
            XposedHelpers.setAdditionalInstanceField(it.thisObject, "mDoubleTapControllerEx", mDoubleTapControllerEx)
        }
        "com.miui.home.launcher.Workspace".hookBeforeMethod("dispatchTouchEvent", MotionEvent::class.java
        ) {
            val mDoubleTapControllerEx = XposedHelpers.getAdditionalInstanceField(it.thisObject, "mDoubleTapControllerEx") as DoubleTapController
            if (!mDoubleTapControllerEx.isDoubleTapEvent(it.args[0] as MotionEvent)) return@hookBeforeMethod
            val mCurrentScreenIndex = it.thisObject.getIntField("mCurrentScreenIndex")
            val cellLayout = it.thisObject.callMethod("getCellLayout", mCurrentScreenIndex)
            if (cellLayout != null) if (cellLayout.callMethod("lastDownOnOccupiedCell") as Boolean) return@hookBeforeMethod
            if (it.thisObject.callMethod("isInNormalEditingMode") as Boolean) return@hookBeforeMethod
            val context = it.thisObject.callMethod("getContext") as Context
            context.sendBroadcast(Intent("com.miui.app.ExtraStatusBarManager.action_TRIGGER_TOGGLE").putExtra("com.miui.app.ExtraStatusBarManager.extra_TOGGLE_ID", 10))
        }
    }
}