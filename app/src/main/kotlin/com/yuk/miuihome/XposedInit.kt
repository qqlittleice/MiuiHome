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
                val mLayoutResId = (it.thisObject.getObjectField("mAllAppsSetting"))?.getObjectField("mLayoutResId")
                val mWidgetLayoutResId = (it.thisObject.getObjectField("mAllAppsSetting"))?.getObjectField("mWidgetLayoutResId")
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
        SetDeviceLevel().init()  // 设置设备分级等
        ModifyBlurLevel().init()  // 后台模糊级别
        EnableSmoothAnimation().init()  // 平滑动画
        EnableBlurWhenOpenFolder().init()  // 文件夹模糊
        EnableMamlDownload().init()  // 水波纹下载
        EnableClockGadget().init()  // 时钟常显
        ModifyAnimDurationRatio().init()  // 手势动画速度
        EnableHideStatusBarWhenEnterRecents().init()  // 后台隐藏状态栏
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
        ModifyDoubleTapToSleep().init()  // 双击锁屏
        ModifyUnlockHotseatIcon().init()  // 解除底栏图标数量限制
        HookSystemProperties().init()  // Prop相关
        ModifyBlurRadius().init()  // 模糊半径
        ModifyIconTitleFontColor().init()  // 应用标题文本颜色
        AlwaysBlurWallpaper().init()  // 始终模糊壁纸
        ModifyRecents().init()  // 后台元素相关
        ModifyIconTitleTopMargin().init()  // 应用图标与标题距离
        ModifyShortcutItemCount().init()  // 解除Shortcut数量限制
        ModifyPadA12DockBlur().init()  // 安卓12平板Dock模糊
        EnableFolderIconBlur().init()  //安卓12小文件夹模糊
        ModifyAppReturnBlur().init()  // 应用返回桌面模糊
        ModifyDockHook().init()  // 安卓12底栏设置
        EnableAllAppsContainerViewBlur().init()  // 安卓12抽屉模糊
        ResourcesHook().init() // 资源相关
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
