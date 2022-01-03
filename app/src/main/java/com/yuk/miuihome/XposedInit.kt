package com.yuk.miuihome

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.content.res.XModuleResources
import android.os.Bundle
import android.view.View
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.yuk.miuihome.module.*
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.HomeContext
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.Method

class XposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
        moduleRes = getModuleRes(modulePath)
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != Config.hookPackage) return
        Application::class.java.hookBeforeMethod(
            "attach",
            Context::class.java
        ) {
            HomeContext.context = it.args[0] as Context
            HomeContext.classLoader = HomeContext.context.classLoader
            HomeContext.application = it.thisObject as Application
            CrashRecord.init(HomeContext.context)
            doHook()
        }
        Application::class.java.hookAfterMethod(
            "attach",
            Context::class.java
        ) {
            startOnlineLog()
            checkVersionName()
            checkAlpha()
            checkVersionCode()
            checkWidgetLauncher()
            checkMiuiVersion()
        }
    }

    private fun doHook() {
        "android.app.Instrumentation".findClass().hookAfterAllMethods(
            "newActivity") { HomeContext.activity = it.result as Activity }
        "com.miui.home.settings.MiuiHomeSettings".hookAfterMethod(
            "onCreatePreferences", Bundle::class.java, String::class.java
        ) {
            (it.thisObject.getObjectField("mDefaultHomeSetting")).apply {
                setObjectField("mTitle", moduleRes.getString(R.string.ModuleSettings))
                setObjectField("mClickListener", object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        if (OwnSP.ownSP.getBoolean("isFirstUse", true))
                            SettingDialog().firstUseDialog()
                        else
                            SettingDialog().showSettingDialog()
                    }
                })
            }
        }
        DisableLog().init()
        SetDeviceLevel().init()
        ModifyBlurLevel().init()
        EnableSmoothAnimation().init()
        EnableBlurWhenOpenFolder().init()
        EnableMamlDownload().init()
        EnableClockGadget().init()
        ModifyAnimDurationRatio().init()
        EnableHideStatusBarWhenEnterRecents().init()
        EnableSearchBarBlur().init()
        EnableRecentsViewHorizontal().init()
        DisableRecentsViewWallpaperDarken().init()
        ModifyHideWidgetTitles().init()
        AllowWidgetToMinus().init()
        AlwaysShowMIUIWidget().init()
        ModifyTaskVertical().init()
        ModifyTaskHorizontal().init()
        EnableSimpleAnimation().init()
        ModifyInfiniteScroll().init()
        ResourcesHook().init()
        ModifyCloseFolderOnLaunch().init()
        ModifyShowDockIconTitles().init()
        EnableDockIconShadow().init()
        AllowAllAppsToUseSmallWindow().init()
        EnableLowEndDeviceUseMIUIWidgets().init()
        DisableRecommendServer().init()
        ModifyHideSeekPoints().init()
        ModifyCategory().init()
        ModifyFolderColumnsCount().init()
        ModifyIconTitleFontSize().init()
        ModifyDockHook().init()
        ModifyDoubleTapToSleep().init()
        ModifyUnlockHotseatIcon().init()
        HookSystemProperties().init()
        ModifyAppReturnBlur().init()
        ModifyBlurRadius().init()
        //CustomHook.init()
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        if (!BuildConfig.useResHook) return
        if (resparam.packageName != Config.hookPackage) return
        hasHookPackageResources = true
        ResHook(resparam).init()
    }

    private fun startOnlineLog() {
        AppCenter.start(HomeContext.application, "fd3fd6d6-bc0d-40d1-bc1b-63b6835f9581", Analytics::class.java, Crashes::class.java)
    }

    fun checkVersionName(): String {
        return HomeContext.context.packageManager.getPackageInfo(HomeContext.context.packageName, 0).versionName
    }

    private fun checkAlpha() {
        HomeContext.isAlpha = if (!checkVersionName().contains("RELEASE", ignoreCase = true)) {
            checkVersionName().contains("ALPHA", ignoreCase = true)
        } else {
            false
        }
    }

    fun checkMiuiVersion(): String {
        return getProp("ro.miui.ui.version.name")
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

@SuppressLint("PrivateApi")
fun getProp(key: String): String {
    lateinit var value: String
    try {
        val clazz = Class.forName("android.os.SystemProperties")
        val get: Method = clazz.getMethod("get", String::class.java)
        value = get.invoke(clazz, key).toString()
    } catch (e: Exception) {
    }
    return value
}