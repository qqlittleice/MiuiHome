package com.yuk.miuihome

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.content.res.XModuleResources
import android.os.Bundle
import android.view.View
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.yuk.miuihome.module.*
import com.yuk.miuihome.module.view.utils.ActivityHelper
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.HomeContext
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {
    companion object {
        lateinit var modulePath: String
        lateinit var moduleRes: Resources
        var hasHookPackageResources = false
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
        moduleRes = getModuleRes(modulePath)
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (lpparam.packageName) {
            Config.hookPackage -> {
                Application::class.java.hookBeforeMethod("attach", Context::class.java
                ) {
                    HomeContext.context = it.args[0] as Context
                    HomeContext.classLoader = HomeContext.context.classLoader
                    HomeContext.application = it.thisObject as Application
                    CrashRecord.init(HomeContext.context)
                    doHook()
                }
                Application::class.java.hookAfterMethod("attach", Context::class.java
                ) {
                    startOnlineLog()
                    checkVersionName()
                    checkAlpha()
                    checkVersionCode()
                    checkWidgetLauncher()
                    checkMiuiVersion()
                }
                Application::class.java.hookAfterMethod("onCreate") {
                    ActivityHelper.initSubActivity()
                }
                if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: [com.miui.home] hook success")
            }
            "com.milink.service" -> {
                XposedHelpers.findAndHookMethod("com.miui.circulate.world.auth.AuthUtil", lpparam.classLoader, "doPermissionCheck", String::class.java, String::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            param.result = null
                        }
                    })
                XposedHelpers.findAndHookMethod("com.miui.circulate.world.utils.GetKeyUtil", lpparam.classLoader, "doWhiteListAuth", String::class.java, String::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            param.result = true
                        }
                    })
                if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: [com.milink.service] hook success")
            }
            else -> return
        }
    }

    private fun doHook() {
        if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: MiuiLauncher version = ${checkVersionName()}(${checkVersionCode()})")
        "com.miui.home.settings.MiuiHomeSettingActivity".hookAfterMethod("onCreate", Bundle::class.java) {
            HomeContext.settingActivity = it.thisObject as Activity
        }
        "com.miui.home.settings.MiuiHomeSettings".hookAfterMethod(
            "onCreatePreferences", Bundle::class.java, String::class.java
        ) {
            (it.thisObject.getObjectField("mDefaultHomeSetting")).apply {
                setObjectField("mTitle", moduleRes.getString(R.string.ModuleSettings))
                setObjectField("mClickListener", object : View.OnClickListener {
                    override fun onClick(v: View) {
//                        val intent = Intent(HomeContext.context, HookSettingsActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        HomeContext.context.startActivity(intent)
                        if (OwnSP.ownSP.getBoolean("isFirstUse", true)) SettingDialog().firstUseDialog() else SettingDialog().showSettingDialog()
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
        ModifyIconTitleFontColor().init()
        //CustomHook.init()
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        if (resparam.packageName != Config.hookPackage) return
        hasHookPackageResources = true
        ResHook(resparam).init()
        if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: Resources hook success")
    }

    private fun startOnlineLog() {
        AppCenter.start(HomeContext.application, "fd3fd6d6-bc0d-40d1-bc1b-63b6835f9581", Analytics::class.java, Crashes::class.java)
    }

    fun checkVersionName(): String {
        return HomeContext.context.packageManager.getPackageInfo(HomeContext.context.packageName, 0).versionName

    }

    fun checkAlpha(): Boolean {
        return (checkVersionName().contains("ALPHA", ignoreCase = true))
    }

    fun checkMiuiVersion(): String {
        return getProp("ro.miui.ui.version.name")
    }

    fun checkVersionCode(): Long {
        return HomeContext.context.packageManager.getPackageInfo(HomeContext.context.packageName, 0).longVersionCode

    }

    fun checkWidgetLauncher(): Boolean {
        val checkList = arrayListOf(
                "com.miui.home.launcher.widget.MIUIAppWidgetInfo",
                "com.miui.home.launcher.LauncherAppWidgetInfo",
                "com.miui.home.launcher.MIUIWidgetUtil"
        )
        return try {
            for (item in checkList) XposedHelpers.findClass(item, HomeContext.classLoader)
            if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: Widget version launcher")
            true
        } catch (e: XposedHelpers.ClassNotFoundError) {
            if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: Not widget version launcher")
            false
        }
    }

    private fun getModuleRes(path: String): Resources {
        return XModuleResources.createInstance(path, null)
    }
}

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@SuppressLint("PrivateApi")
fun getProp(key: String): String {
    return Class.forName("android.os.SystemProperties").getMethod("get", String::class.java).invoke(Class.forName("android.os.SystemProperties"), key).toString()
}