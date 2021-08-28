package com.yuk.miuihome

import android.app.Application
import android.content.Context
import androidx.annotation.Keep
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage

@Keep
class XposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != Config.hookPackage) return
        XposedHelpers.findAndHookMethod("com.miui.home.launcher.Application", lpparam.classLoader, "attachBaseContext", Context::class.java, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                HomeContext.application = param.thisObject as Application
                HomeContext.context = param.args[0] as Context
                HomeContext.classLoader = HomeContext.context.classLoader
                HomeContext.myRes = ResInject().init()
                checkAlpha()
                MainHook().doHook()
            }
        })
    }

    private fun checkAlpha() {
        val pkgInfo = HomeContext.context.packageManager.getPackageInfo(HomeContext.context.packageName, 0)
        if (! pkgInfo.versionName.contains("RELEASE", ignoreCase = true)) {
            HomeContext.isAlpha = pkgInfo.versionName.contains("ALPHA", ignoreCase = true)
        } else {
            HomeContext.isAlpha = false
        }
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        if (resparam.packageName != Config.hookPackage) return
        hasHookPackageResources = true
        ResHook(resparam).init()
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
    }

    companion object {
        lateinit var modulePath: String
        var hasHookPackageResources = false
    }

}
