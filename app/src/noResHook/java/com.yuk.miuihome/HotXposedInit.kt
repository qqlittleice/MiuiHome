package com.yuk.miuihome

import com.yuk.miuihome.utils.ktx.callMethod
import dalvik.system.PathClassLoader
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.IXposedHookZygoteInit.StartupParam
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import java.io.File

class HotXposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit {

    override fun initZygote(startupParam: StartupParam) {
        modulePath = startupParam.modulePath
    }

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        moduleInstance?.run {
            val param = StartupParam::class.java.newInstance() as StartupParam
            param.modulePath = moduleApkFile.absolutePath
            param.startsSystemServer = false
            runCatching {
                callMethod("initZygote", param)
            }
            runCatching {
                callMethod("handleLoadPackage", lpparam)
            }
        }
    }

    companion object {
        private const val REAL_XPOSED_INIT = "com.yuk.miuihome.XposedInit"
        lateinit var modulePath: String

        val moduleApkFile: File = File(modulePath)

        val moduleInstance: Any?
            get() {
                moduleApkFile.let {
                    if (!it.exists()) return null
                    val classLoader = PathClassLoader(it.absolutePath, XposedBridge.BOOTCLASSLOADER)
                    return classLoader.loadClass(REAL_XPOSED_INIT)?.newInstance()
                }
            }
    }
}