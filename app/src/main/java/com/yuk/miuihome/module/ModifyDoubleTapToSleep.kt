package com.yuk.miuihome.module

import android.content.Context
import android.content.Intent
import de.robv.android.xposed.XposedHelpers
import android.view.MotionEvent
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookAfterAllConstructors
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class ModifyDoubleTapToSleep {

    fun init() {
        if (ownSP.getBoolean("doubleTap", false)) {
            val workspace = "com.miui.home.launcher.Workspace".findClass()
            workspace.hookAfterAllConstructors {
                var mDoubleTapControllerEx = XposedHelpers.getAdditionalInstanceField(
                    it.thisObject,
                    "mDoubleTapControllerEx"
                )
                if (mDoubleTapControllerEx != null) return@hookAfterAllConstructors
                mDoubleTapControllerEx = DoubleTapController((it.args[0] as Context))
                XposedHelpers.setAdditionalInstanceField(
                    it.thisObject,
                    "mDoubleTapControllerEx",
                    mDoubleTapControllerEx
                )
            }
            "com.miui.home.launcher.Workspace".hookBeforeMethod(
                "dispatchTouchEvent",
                MotionEvent::class.java
            ) {
                val mDoubleTapControllerEx = XposedHelpers.getAdditionalInstanceField(
                    it.thisObject,
                    "mDoubleTapControllerEx"
                ) as DoubleTapController
                if (!mDoubleTapControllerEx.isDoubleTapEvent(it.args[0] as MotionEvent)) return@hookBeforeMethod
                val mCurrentScreenIndex = XposedHelpers.getIntField(
                    it.thisObject,
                    "mCurrentScreenIndex"
                )
                val cellLayout =
                    XposedHelpers.callMethod(it.thisObject, "getCellLayout", mCurrentScreenIndex)
                if (XposedHelpers.callMethod(
                        cellLayout,
                        "lastDownOnOccupiedCell"
                    ) as Boolean
                ) return@hookBeforeMethod
                if (XposedHelpers.callMethod(
                        it.thisObject,
                        "isInNormalEditingMode"
                    ) as Boolean
                ) return@hookBeforeMethod
                mDoubleTapControllerEx.onDoubleTapEvent()
                val context = XposedHelpers.callMethod(it.thisObject, "getContext") as Context
                context.sendBroadcast(
                    Intent("com.miui.app.ExtraStatusBarManager.action_TRIGGER_TOGGLE")
                        .putExtra("com.miui.app.ExtraStatusBarManager.extra_TOGGLE_ID", 10)
                )
            }
        }
    }
}