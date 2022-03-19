package com.yuk.miuihome.module

import com.github.kyuubiran.ezxhelper.init.InitFields.ezXClassLoader
import com.yuk.miuihome.BuildConfig
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError

interface BaseClassAndMethodCheck {

    fun classAndMethodList(): ArrayList<String>

    private fun getAllMethods(cls: Class<*>): ArrayList<String> {
        val methodNameList = ArrayList<String>()
        var loopCls = cls
        while (loopCls != Any::class.java) {
            for (item in cls.methods) methodNameList.add(item.name)
            loopCls = cls.superclass
        }
        return methodNameList
    }

    fun checkClassAndMethodExist(): Boolean {
        val list = classAndMethodList()
        if (list.size % 2 != 0)
            throw RuntimeException("checkClassAndMethodExist() -> ClassAndMethodList.size should be an even number")
        try {
            for (i in 0 until list.size step 2) {
                val cls = XposedHelpers.findClass(list[i], ezXClassLoader)
                if (list[i + 1] !in getAllMethods(cls)) return false
            }
        } catch (e: ClassNotFoundError) {
            if (BuildConfig.DEBUG) {
                XposedBridge.log("MiuiHome: $list Class not found")
            }
            return false
        }
        if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: $list Class found successfully")
        return true
    }
}

inline fun BaseClassAndMethodCheck.runWithChecked(crossinline code: () -> Unit) =
    if (checkClassAndMethodExist()) code() else null
