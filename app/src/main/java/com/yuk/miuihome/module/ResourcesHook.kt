package com.yuk.miuihome.module

import android.content.res.Resources
import com.yuk.miuihome.utils.HomeContext
import com.yuk.miuihome.utils.ResourcesHookData
import com.yuk.miuihome.utils.ResourcesHookMap
import com.yuk.miuihome.utils.ktx.hookBeforeMethod
import de.robv.android.xposed.XC_MethodHook

class ResourcesHook {

    private fun hook(param: XC_MethodHook.MethodHookParam) {
        try {
            val res = HomeContext.context.resources
            val resName = res.getResourceEntryName(param.args[0] as Int)
            val resType = res.getResourceTypeName(param.args[0] as Int)
            if (hookMap.isKeyExist(resName)) {
                if (hookMap[resName]?.type == resType) {
                    param.result = hookMap[resName]?.afterValue
                }
            }
        } catch (ignore: Exception) {
        }
    }

    fun init() {
        Resources::class.java.hookBeforeMethod("getBoolean", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getDimension", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getDimensionPixelOffset", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getDimensionPixelSize", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getInteger", Int::class.javaPrimitiveType) { hook(it) }
        Resources::class.java.hookBeforeMethod("getText", Int::class.javaPrimitiveType) { hook(it) }
    }

    companion object {
        val hookMap = ResourcesHookMap<String, ResourcesHookData>()
    }
}