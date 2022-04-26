package com.yuk.miuihome.module

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.github.kyuubiran.ezxhelper.init.InitFields
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.github.kyuubiran.ezxhelper.utils.*
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.utils.ktx.px2dp
import com.zhenxiang.blur.WindowBlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

class ModifyDockHook {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("dockSettings", false)) return
        try {
            val deviceConfigClass = loadClass("com.miui.home.launcher.DeviceConfig")
            val launcherClass = loadClass("com.miui.home.launcher.Launcher")
            val deviceWidth = px2dp(appContext.resources.displayMetrics.widthPixels)
            // Dock距屏幕两侧
            findMethod(deviceConfigClass) {
                name == "calcSearchBarWidth" && parameterTypes[0] == Context::class.java
            }.hookReturnConstant(dp2px(deviceWidth - OwnSP.ownSP.getFloat("dockSide", 3.0f) * 10))
            // Dock距屏幕底部
            findMethod(deviceConfigClass) {
                name == "calcSearchBarMarginBottom" && parameterTypes[0] == Context::class.java && parameterTypes[1] == Boolean::class.java
            }.hookReturnConstant(dp2px(OwnSP.ownSP.getFloat("dockBottom", 2.3f) * 10))
            // 图标距屏幕底部
            findMethod(deviceConfigClass) {
                name == "calcHotSeatsMarginBottom" && parameterTypes[0] == Context::class.java && parameterTypes[1] == Boolean::class.java && parameterTypes[2] == Boolean::class.java
            }.hookReturnConstant(dp2px(OwnSP.ownSP.getFloat("dockIconBottom", 3.5f) * 10))
            // 页面指示器距离图标距离
            findMethod(deviceConfigClass) {
                name == "calcHotSeatsMarginTop" && parameterTypes[0] == Context::class.java && parameterTypes[1] == Boolean::class.java
            }.hookReturnConstant(dp2px(OwnSP.ownSP.getFloat("dockMarginTop", 0.6f) * 10))
            // 页面指示器距离屏幕底部
            findMethod(deviceConfigClass) {
                name == "getWorkspaceIndicatorMarginBottom"
            }.hookReturnConstant(dp2px(OwnSP.ownSP.getFloat("dockMarginBottom", 11.0f) * 10))
            // 宽度变化量
            findMethod(deviceConfigClass) {
                name == "getSearchBarWidthDelta"
            }.hookReturnConstant(0)

            if (Build.VERSION.SDK_INT < 31) {
                XposedHelpers.findAndHookMethod( // TODO
                    "com.miui.home.launcher.Launcher",
                    InitFields.ezXClassLoader,
                    "onResume",
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: MethodHookParam) {
                            val searchBarObject = param.thisObject.invokeMethodAuto("getSearchBar") as FrameLayout
                            val searchBarDesktop = searchBarObject.getChildAt(0) as RelativeLayout
                            val rippleDrawable = searchBarDesktop.background as RippleDrawable
                            val gradientDrawable = rippleDrawable.getDrawable(0) as GradientDrawable
                            gradientDrawable.cornerRadius = dp2px(OwnSP.ownSP.getFloat("dockRadius", 2.5f) * 10).toFloat()
                            gradientDrawable.setStroke(0, 0)
                            rippleDrawable.setDrawable(0, gradientDrawable)
                            searchBarDesktop.background = rippleDrawable
                        }
                    })
            }
            XposedHelpers.findAndHookMethod( // TODO
                "com.miui.home.launcher.Launcher",
                InitFields.ezXClassLoader,
                "onCreate",
                Bundle::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val activity = param.thisObject as Activity
                        val view = activity.findViewById(activity.resources.getIdentifier("recents_container", "id", Config.hostPackage)) as View
                        val isFolderShowing = activity.invokeMethodAuto("isFolderShowing") as Boolean
                        val isInEditing = activity.invokeMethodAuto("isInEditing") as Boolean
                        val searchBarObject = param.thisObject.invokeMethodAuto("getSearchBar") as FrameLayout
                        val searchBarDrawer = searchBarObject.getChildAt(1) as RelativeLayout
                        val searchBarDesktop = searchBarObject.getChildAt(0) as RelativeLayout
                        val searchBarContainer = searchBarObject.parent as FrameLayout
                        val searchEdgeLayout = searchBarContainer.parent as FrameLayout
                        // 重新给搜索框容器排序
                        searchEdgeLayout.removeView(searchBarContainer)
                        searchEdgeLayout.addView(searchBarContainer, 0)
                        // 清空搜索图标和小爱同学
                        searchBarDesktop.removeAllViews()
                        // 修改高度
                        searchBarObject.layoutParams.height = dp2px((OwnSP.ownSP.getFloat("dockHeight", 7.9f) * 10))
                        // 设置 A11 圆角
                        if (Build.VERSION.SDK_INT < 31) {
                            val rippleDrawable = searchBarDesktop.background as RippleDrawable
                            val gradientDrawable = rippleDrawable.getDrawable(0) as GradientDrawable
                            gradientDrawable.cornerRadius = dp2px(OwnSP.ownSP.getFloat("dockRadius", 2.5f) * 10).toFloat()
                            gradientDrawable.setStroke(0, 0)
                            rippleDrawable.setDrawable(0, gradientDrawable)
                            searchBarDesktop.background = rippleDrawable
                        }
                        // 添加 A12 模糊
                        if (OwnSP.ownSP.getBoolean("searchBarBlur", false) && Build.VERSION.SDK_INT == 31) {
                            searchBarObject.removeAllViews()
                            val blur = WindowBlurFrameLayout(searchBarObject.context)
                            blur.blurController.apply {
                                backgroundColour = Color.parseColor("#44FFFFFF")
                                cornerRadius = CornersRadius.all(dp2px((OwnSP.ownSP.getFloat("dockRadius", 2.5f) * 10)).toFloat())
                            }
                            if (view.visibility == View.GONE && !isInEditing  && !isFolderShowing) searchBarObject.addView(blur)
                        }
                        // 修改应用列表搜索框
                        val mAllAppViewField = launcherClass.getDeclaredField("mAppsView")
                        mAllAppViewField.isAccessible = true
                        val mAllAppView = mAllAppViewField.get(param.thisObject) as RelativeLayout
                        val mAllAppSearchView = mAllAppView.getChildAt(mAllAppView.childCount - 1) as FrameLayout
                        mAllAppSearchView.addView(searchBarDrawer)
                        searchBarDrawer.bringToFront()
                        val layoutParams = searchBarDrawer.layoutParams as FrameLayout.LayoutParams
                        searchBarDrawer.layoutParams.height = dp2px(43f)
                        layoutParams.leftMargin = dp2px(15f)
                        layoutParams.rightMargin = dp2px(15f)
                        searchBarDrawer.layoutParams = layoutParams
                    }
                })
        } catch (e: XposedHelpers.ClassNotFoundError) {
            Log.ex(e)
        }
    }
}