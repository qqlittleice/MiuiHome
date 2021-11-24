package com.yuk.miuihome

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.content.res.XModuleResources
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.yuk.miuihome.utils.LogUtil
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
        moduleRes = getModuleRes(modulePath)
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (lpparam.packageName) {
            Config.packageName -> {
                XposedHelpers.findAndHookMethod("com.yuk.miuihome.activity.MainActivity", lpparam.classLoader, "isModuleEnable", object : XC_MethodHook() {
                    override fun afterHookedMethod(lpparam: MethodHookParam) {
                        lpparam.result = true
                    }
                })
            }
            Config.hookPackage -> {
                XposedHelpers.findAndHookMethod(Application::class.java, "attach", Context::class.java, object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        HomeContext.context = param.args[0] as Context
                        HomeContext.classLoader = HomeContext.context.classLoader
                        HomeContext.application = param.thisObject as Application
                        CrashRecord.init(HomeContext.context)
                        startOnlineLog()
                        checkAlpha()
                        checkVersionCode()
                        checkWidgetLauncher()
                        MainHook().doHook()
                    }
                })
            }
            else -> {
                return
            }
        }
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        if (resparam.packageName != Config.hookPackage) return
        hasHookPackageResources = true
        ResHook(resparam).init()
    }

    private fun startOnlineLog() {
        AppCenter.start(HomeContext.application, "fd3fd6d6-bc0d-40d1-bc1b-63b6835f9581", Analytics::class.java, Crashes::class.java)
    }

    private fun checkAlpha() {
        val pkgInfo = HomeContext.context.packageManager.getPackageInfo(HomeContext.context.packageName, 0)
        HomeContext.isAlpha = if (!pkgInfo.versionName.contains("RELEASE", ignoreCase = true)) {
            pkgInfo.versionName.contains("ALPHA", ignoreCase = true)
        } else {
            false
        }
    }

    private fun checkVersionCode() {
        try {
            HomeContext.versionCode = HomeContext.context.packageManager.getPackageInfo(HomeContext.context.packageName, 0).longVersionCode
        } catch (e: Exception) {
            LogUtil.e(e)
            HomeContext.versionCode = -1L
        }
    }

    private fun checkWidgetLauncher() {
        val checkList = arrayListOf(
                "com.miui.home.launcher.widget.MIUIAppWidgetInfo",
                "com.miui.home.launcher.LauncherAppWidgetInfo",
                "com.miui.home.launcher.MIUIWidgetUtil"
        )
        try {
            for (item in checkList) {
                XposedHelpers.findClass(item, HomeContext.classLoader)
            }
            HomeContext.isWidgetLauncher = true
        } catch (e: XposedHelpers.ClassNotFoundError) {
            HomeContext.isWidgetLauncher = false
        }
    }

    private fun getModuleRes(path: String): Resources {
        return XModuleResources.createInstance(path, null)
    }

    companion object {
        lateinit var modulePath: String
        lateinit var moduleRes: Resources
        var hasHookPackageResources = false
    }
}