package com.yuk.miuihome

import android.app.Application
import android.content.Context
import androidx.annotation.Keep
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.yuk.miuihome.Config.myself
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError

class XposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {

    @Keep
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (lpparam.packageName) {
            myself -> {
                XposedHelpers.findAndHookMethod(
                    "com.yuk.miuihome.activity.MainActivity",
                    lpparam.classLoader,
                    "moduleEnable",
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(lpparam: MethodHookParam) {
                            lpparam.result = true
                        }
                    }
                )
            }
            Config.hookPackage -> {
                XposedHelpers.findAndHookMethod(
                    "com.miui.home.launcher.Application",
                    lpparam.classLoader,
                    "attachBaseContext",
                    Context::class.java,
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            HomeContext.application = param.thisObject as Application
                            HomeContext.context = param.args[0] as Context
                            HomeContext.classLoader = HomeContext.context.classLoader
                            HomeContext.resInstance = ResInject().init()
                            startOnlineLog()
                            checkAlpha()
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

    fun startOnlineLog() {
        AppCenter.start(
            HomeContext.application,
            "fd3fd6d6-bc0d-40d1-bc1b-63b6835f9581",
            Analytics::class.java,
            Crashes::class.java
        )
    }

    fun checkAlpha() {
        val pkgInfo =
            HomeContext.context.packageManager.getPackageInfo(HomeContext.context.packageName, 0)
        if (!pkgInfo.versionName.contains("RELEASE", ignoreCase = true)) {
            HomeContext.isAlpha = pkgInfo.versionName.contains("ALPHA", ignoreCase = true)
        } else {
            HomeContext.isAlpha = false
        }
    }

    fun checkWidgetLauncher() {
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
        } catch (e: ClassNotFoundError) {
            HomeContext.isWidgetLauncher = false
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