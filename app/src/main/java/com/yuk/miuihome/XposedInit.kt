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
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.init.InitFields.moduleRes
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.AbstractCrashesListener
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.crashes.ingestion.models.ErrorAttachmentLog
import com.microsoft.appcenter.crashes.model.ErrorReport
import com.yuk.miuihome.module.*
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.HomeContext
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*
import com.yuk.miuihome.view.HookSettingsActivity
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {
    companion object {
        var hasHookPackageResources = false
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        EzXHelperInit.initZygote(startupParam)
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (lpparam.packageName) {
            Config.hookPackage -> {
                Application::class.java.hookBeforeMethod("attach", Context::class.java
                ) {
                    EzXHelperInit.initHandleLoadPackage(lpparam)
                    HomeContext.context = it.args[0] as Context
                    EzXHelperInit.initAppContext(HomeContext.context)
                    HomeContext.classLoader = HomeContext.context.classLoader
                    HomeContext.application = it.thisObject as Application
                    CrashRecord.init(HomeContext.context)
                    doHook()
                }
                Application::class.java.hookAfterMethod("attach", Context::class.java
                ) {
                    startAppCenter()
                    checkVersionName()
                    checkIsAlpha()
                    checkVersionCode()
                    checkWidgetLauncher()
                    checkMiuiVersion()
                    checkAndroidVersion()
                }
                Application::class.java.hookAfterMethod("onCreate") {
                    ActivityHelper.initSubActivity()
                }
                if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: [com.miui.home] hook success")
            }
            else -> return
        }
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        if (resparam.packageName != Config.hookPackage) return
        hasHookPackageResources = true
        ResHook(resparam).init()
        if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: Resources hook success")
    }

    private fun getModuleRes(path: String): Resources {
        return XModuleResources.createInstance(path, null)
    }

    private fun doHook() {
        if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: MiuiLauncher version = ${checkVersionName()}(${checkVersionCode()})")
        "com.miui.home.settings.MiuiHomeSettingActivity".hookAfterMethod("onCreate", Bundle::class.java) {
            HomeContext.Activity = it.thisObject as Activity
        }
        "com.miui.home.settings.MiuiHomeSettings".hookAfterMethod(
            "onCreatePreferences", Bundle::class.java, String::class.java
        ) {
            (it.thisObject.getObjectField("mDefaultHomeSetting")).apply {
                setObjectField("mTitle", moduleRes.getString(R.string.ModuleSettings))
                setObjectField("mClickListener", object : View.OnClickListener {
                    override fun onClick(v: View) {
                        val intent = Intent(HomeContext.context, HookSettingsActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        HomeContext.context.startActivity(intent)
                    }
                })
            }
        }
        DisableLog().init()
        SetDeviceLevel().init()  // 设置设备分级等
        ModifyBlurLevel().init()  // 后台模糊级别
        EnableSmoothAnimation().init()  // 平滑动画
        EnableBlurWhenOpenFolder().init()  // 文件夹模糊
        EnableMamlDownload().init()  // 水波纹下载
        EnableClockGadget().init()  // 时钟常显
        ModifyAnimDurationRatio().init()  // 手势动画速度
        EnableHideStatusBarWhenEnterRecents().init()  // 后台隐藏状态栏
        EnableSearchBarBlur().init()  // 搜索框模糊
        EnableRecentsViewHorizontal().init()  // 启用横屏后台
        DisableRecentsViewWallpaperDarken().init()  // 取消壁纸压暗
        ModifyHideWidgetTitles().init()  // 隐藏小部件标题
        AllowWidgetToMinus().init()  // 允许安卓小部件移动到负一屏
        AlwaysShowMIUIWidget().init()  // 在安卓小部件中显示MIUI小部件
        ModifyTaskVertical().init()  // 平铺后台卡片大小
        ModifyTaskHorizontal().init()  // 瀑布后台卡片大小
        EnableSimpleAnimation().init()  // 简单动画
        ModifyInfiniteScroll().init()  // 桌面无限滚动
        ModifyCloseFolderOnLaunch().init()  // 自动关闭文件夹
        ModifyShowDockIconTitles().init()  // 显示底栏应用标题
        EnableDockIconShadow().init()  // 图标阴影、倒影
        AllowAllAppsToUseSmallWindow().init()  // 允许所有应用作为小窗使用
        EnableLowEndDeviceUseMIUIWidgets().init()  // 允许低端机使用MIUI小部件
        DisableRecommendServer().init() // 文件夹推荐广告
        ModifyHideSeekPoints().init()  // 隐藏页面指示器
        ModifyCategory().init()  // 抽屉相关
        ModifyFolderColumnsCount().init()  // 文件夹排列数
        ModifyIconTitleFontSize().init()  // 应用标题文本大小
        ModifyDockHook().init()  // 搜索框 -> 底栏 设置
        ModifyDoubleTapToSleep().init()  // 双击锁屏
        ModifyUnlockHotseatIcon().init()  // 解除底栏图标数量限制
        HookSystemProperties().init()  // Prop相关
        ModifyAppReturnBlur().init()  // 应用返回模糊
        ModifyBlurRadius().init()  // 模糊半径
        ModifyIconTitleFontColor().init()  // 应用标题文本颜色
        AlwaysBlurWallpaper().init()  // 始终模糊壁纸
        ModifyRecents().init()  // 后台元素相关
        ModifyIconTitleTopMargin().init()  // 应用图标与标题距离
        ModifyShortcutItemCount().init()  // 解除Shortcut数量限制
        ResourcesHook().init()  //  资源相关
        //CustomHook.init()
    }

    private fun startAppCenter() {
        if (OwnSP.ownSP.getBoolean("appCenter", false)) {
            AppCenter.start(HomeContext.application, "fd3fd6d6-bc0d-40d1-bc1b-63b6835f9581", Analytics::class.java,Crashes::class.java)
            Crashes.setListener(object : AbstractCrashesListener() {
                override fun getErrorAttachments(report: ErrorReport): MutableIterable<ErrorAttachmentLog> {
                    val textLog = ErrorAttachmentLog.attachmentWithText("Module:\n${BuildConfig.APPLICATION_ID} - ${BuildConfig.BUILD_TYPE}\n${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})\nUser:\nAndroid ${checkAndroidVersion()}\nMiuiHome ${checkVersionName()}(${checkVersionCode()})", "debug.txt")
                    return mutableListOf(textLog)
                }
            })
        } else {
            if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: Disable App Center")
        }
    }

    fun checkVersionName(): String {
        return HomeContext.context.packageManager.getPackageInfo(HomeContext.context.packageName, 0).versionName

    }

    fun checkIsAlpha(): Boolean {
        return (checkVersionName().contains("ALPHA", ignoreCase = true))
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
        return HomeContext.context.packageManager.getPackageInfo(HomeContext.context.packageName, 0).longVersionCode
    }

    fun checkWidgetLauncher(): Boolean {
        val checkList = arrayListOf(
            "com.miui.home.launcher.widget.MIUIAppWidgetInfo",
            "com.miui.home.launcher.LauncherAppWidgetInfo",
            "com.miui.home.launcher.MIUIWidgetUtil"
        )
        return try {
            for (item in checkList) item.findClass(HomeContext.classLoader)
            if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: Widget version launcher")
            true
        } catch (e: XposedHelpers.ClassNotFoundError) {
            if (BuildConfig.DEBUG) XposedBridge.log("MiuiHome: Not widget version launcher")
            false
        }
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("PrivateApi")
    fun getProp(key: String): String {
        return Class.forName("android.os.SystemProperties").getMethod("get", String::class.java).invoke(Class.forName("android.os.SystemProperties"), key).toString()
    }
}
