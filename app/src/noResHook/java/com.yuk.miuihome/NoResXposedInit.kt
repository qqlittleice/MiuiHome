package com.yuk.miuihome

import com.yuk.miuihome.view.utils.ktx.callMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.IXposedHookZygoteInit.StartupParam
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class NoResXposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit {

    override fun initZygote(startupParam: StartupParam) {
        moduleInstance?.run {
            runCatching {
                callMethod("initZygote", startupParam)
            }
        }
    }

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        moduleInstance?.run {
            runCatching {
                callMethod("handleLoadPackage", lpparam)
            }
        }
    }

    companion object {
        private const val REAL_XPOSED_INIT = "com.yuk.miuihome.XposedInit"

        val moduleInstance: Any?
            get() {
                val classLoader = NoResXposedInit::class.java.classLoader
                return classLoader!!.loadClass(REAL_XPOSED_INIT)?.newInstance()
            }
    }
}