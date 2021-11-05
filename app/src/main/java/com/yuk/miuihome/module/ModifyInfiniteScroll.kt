package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import de.robv.android.xposed.XposedHelpers

class ModifyInfiniteScroll {

    fun init() {
        if (ownSP.getBoolean("infiniteScroll", false)) {
            "com.miui.home.launcher.ScreenView".hookAfterMethod(
                "getSnapToScreenIndex",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            ) {
                if (it.args[0] !== it.result) return@hookAfterMethod
                val screenCount = XposedHelpers.callMethod(it.thisObject, "getScreenCount") as Int
                if (it.args[2] as Int == -1 && it.args[0] as Int == 0) it.result =
                    screenCount else if (it.args[2] as Int == 1 && it.args[0] as Int == screenCount - 1) it.result = 0
            }
            "com.miui.home.launcher.ScreenView".hookAfterMethod(
                "getSnapUnitIndex",
                Int::class.javaPrimitiveType
            ) {
                val mCurrentScreenIndex =
                    XposedHelpers.getIntField(it.thisObject, "mCurrentScreenIndex")
                if (mCurrentScreenIndex != it.result as Int) return@hookAfterMethod
                val screenCount = XposedHelpers.callMethod(it.thisObject, "getScreenCount") as Int
                if (it.result as Int == 0) it.result =
                    screenCount else if (it.result as Int == screenCount - 1) it.result = 0
            }
        }
    }
}