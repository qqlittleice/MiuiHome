package com.yuk.miuihome.custom.hook

import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.LogUtil
import de.robv.android.xposed.XposedHelpers

object CustomHook {

    private fun hookResult(className: String, methodName: String, args: Array<Any>) {
        try {
            XposedHelpers.findAndHookMethod(className, HomeContext.classLoader, methodName, args)
        } catch (e: Exception) {
            LogUtil.e("CustomHook -> hookResult()")
            LogUtil.e(e)
        }
    }

    fun init() {
        try {
        } catch (e: Exception) {
            LogUtil.e(e)
        }
    }

}
