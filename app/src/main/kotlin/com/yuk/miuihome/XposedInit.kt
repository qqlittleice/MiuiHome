package com.yuk.miuihome

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.View
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.github.kyuubiran.ezxhelper.init.InitFields.ezXClassLoader
import com.github.kyuubiran.ezxhelper.init.InitFields.moduleRes
import com.yuk.miuihome.module.*
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.Config.TAG
import com.yuk.miuihome.utils.ktx.*
import com.yuk.miuihome.view.HookSettingsActivity
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage


class XposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        var application: Application? = null
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        EzXHelperInit.initZygote(startupParam)
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (lpparam.packageName) {
            Config.hostPackage -> {
                Application::class.java.hookBeforeMethod("attach", Context::class.java) {
                    EzXHelperInit.apply {
                        initHandleLoadPackage(lpparam)
                        setLogTag(TAG)
                        setToastTag(TAG)
                        initAppContext(it.args[0] as Context)
                        setEzClassLoader(appContext.classLoader)
                        initActivityProxyManager(Config.modulePackage, Config.hostActivityProxy, XposedInit::class.java.classLoader!!, ezXClassLoader)
                        initSubActivity()
                    }
                    CrashRecord.init(appContext)
                    application = it.thisObject as Application
                    doHook()
                }
                Application::class.java.hookAfterMethod("attach", Context::class.java) {
                    checkVersionName()
                    checkIsAlpha()
                    checkVersionCode()
                    checkWidgetLauncher()
                    checkMiuiVersion()
                    checkAndroidVersion()
                    checkIsPadDevice()
                }
                if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: [com.miui.home] hook success")
            }
            else -> return
        }
    }

    private fun doHook() {
        if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: MiuiLauncher version = ${checkVersionName()}(${checkVersionCode()})")
        "com.miui.home.settings.MiuiHomeSettings".findClass().hookAfterAllMethods("onCreatePreferences") {
            try {
                val mLayoutResId = (it.thisObject.getObjectField("mDefaultHomeSetting"))?.getObjectField("mLayoutResId")
                val mWidgetLayoutResId = (it.thisObject.getObjectField("mDefaultHomeSetting"))?.getObjectField("mWidgetLayoutResId")
                val pref = XposedHelpers.newInstance("com.miui.home.settings.preference.ValuePreference".findClass(), appContext).apply {
                    setObjectField("mTitle", "MiuiHome")
                    setObjectField("mOrder", 0)
                    setObjectField("mVisible", true)
                    setObjectField("mLayoutResId", mLayoutResId)
                    setObjectField("mWidgetLayoutResId", mWidgetLayoutResId)
                    setObjectField("mFragment", "MiuiHome")
                    callMethod("setValue",moduleRes.getString(R.string.ModuleSettings))
                    setObjectField("mClickListener", object : View.OnClickListener {
                        override fun onClick(v: View) {
                            val intent = Intent(v.context, HookSettingsActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            v.context.startActivity(intent)
                            context = v.context
                        }
                    })
                    callMethod("setIntent", Intent())
                }
                it.thisObject.callMethod("getPreferenceScreen")?.callMethod("addPreference", pref)
            } catch (e: Throwable) {
                (it.thisObject.getObjectField("mDefaultHomeSetting")).apply {
                    setObjectField("mTitle", moduleRes.getString(R.string.ModuleSettings))
                    callMethod("setValue",moduleRes.getString(R.string.ModuleSettings))
                    setObjectField("mClickListener", object : View.OnClickListener {
                        override fun onClick(v: View) {
                            v.context.startActivity(Intent(v.context, HookSettingsActivity::class.java))
                            context = v.context
                        }
                    })
                }
            }
        }
        DisableLog().init()
        SetDeviceLevel().init()  // ?????????????????????
        ModifyBlurLevel().init()  // ??????????????????
        EnableSmoothAnimation().init()  // ????????????
        EnableBlurWhenOpenFolder().init()  // ???????????????
        EnableMamlDownload().init()  // ???????????????
        EnableClockGadget().init()  // ????????????
        ModifyAnimDurationRatio().init()  // ??????????????????
        EnableHideStatusBarWhenEnterRecents().init()  // ?????????????????????
        EnableRecentsViewHorizontal().init()  // ??????????????????
        DisableRecentsViewWallpaperDarken().init()  // ??????????????????
        ModifyHideWidgetTitles().init()  // ?????????????????????
        AllowWidgetToMinus().init()  // ???????????????????????????????????????
        AlwaysShowMIUIWidget().init()  // ???????????????????????????MIUI?????????
        ModifyTaskVertical().init()  // ????????????????????????
        ModifyTaskHorizontal().init()  // ????????????????????????
        EnableSimpleAnimation().init()  // ????????????
        ModifyInfiniteScroll().init()  // ??????????????????
        ModifyCloseFolderOnLaunch().init()  // ?????????????????????
        ModifyShowDockIconTitles().init()  // ????????????????????????
        EnableDockIconShadow().init()  // ?????????????????????
        AllowAllAppsToUseSmallWindow().init()  // ????????????????????????????????????
        EnableLowEndDeviceUseMIUIWidgets().init()  // ?????????????????????MIUI?????????
        DisableRecommendServer().init() // ?????????????????????
        ModifyHideSeekPoints().init()  // ?????????????????????
        ModifyCategory().init()  // ????????????
        ModifyFolderColumnsCount().init()  // ??????????????????
        ModifyIconTitleFontSize().init()  // ????????????????????????
        ModifyDoubleTapToSleep().init()  // ????????????
        ModifyUnlockHotseatIcon().init()  // ??????????????????????????????
        HookSystemProperties().init()  // Prop??????
        ModifyBlurRadius().init()  // ????????????
        ModifyIconTitleFontColor().init()  // ????????????????????????
        AlwaysBlurWallpaper().init()  // ??????????????????
        ModifyRecents().init()  // ??????????????????
        ModifyIconTitleTopMargin().init()  // ???????????????????????????
        ModifyShortcutItemCount().init()  // ??????Shortcut????????????
        ModifyPadA12DockBlur().init()  // ??????12??????Dock??????
        EnableFolderIconBlur().init()  //??????12??????????????????
        ModifyAppReturnBlur().init()  // ????????????????????????
        ModifyDockHook().init()  // ??????12????????????
        EnableAllAppsContainerViewBlur().init()  // ??????12????????????
        ResourcesHook().init() // ????????????
    }

    fun checkVersionName(): String {
        return appContext.packageManager.getPackageInfo(appContext.packageName, 0).versionName

    }

    fun checkIsAlpha(): Boolean {
        return (checkVersionName().contains("ALPHA", ignoreCase = true))
    }

    fun checkIsPadDevice(): Boolean {
        return XposedHelpers.callStaticMethod("com.miui.home.launcher.common.Utilities".findClass(),"isPadDevice") as Boolean
    }

    fun checkMiuiVersion(): String {
        return when (getProp("ro.miui.ui.version.name")) {
            "V130" -> "13"
            "V125" -> "12.5"
            "V12" -> "12"
            "V11" -> "11"
            "V10" -> "10"
            else -> "?"
        }
    }

    fun checkAndroidVersion(): String {
        return getProp("ro.build.version.release")
    }

    fun checkVersionCode(): Long {
        return appContext.packageManager.getPackageInfo(appContext.packageName, 0).longVersionCode
    }

    @SuppressLint("DiscouragedApi")
    fun getCornerRadiusTop(): Int {
        val resourceId = appContext.resources.getIdentifier("rounded_corner_radius_top", "dimen", "android")
        return if (resourceId > 0) {
            appContext.resources.getDimensionPixelSize(resourceId)
        } else 100
    }

    fun checkWidgetLauncher(): Boolean {
        val checkList = arrayListOf(
            "com.miui.home.launcher.widget.MIUIAppWidgetInfo",
            "com.miui.home.launcher.LauncherAppWidgetInfo",
            "com.miui.home.launcher.MIUIWidgetUtil"
        )
        return try {
            for (item in checkList) item.findClass(ezXClassLoader)
            if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: Widget version launcher")
            true
        } catch (e: XposedHelpers.ClassNotFoundError) {
            if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: Not widget version launcher")
            false
        }
    }
}
