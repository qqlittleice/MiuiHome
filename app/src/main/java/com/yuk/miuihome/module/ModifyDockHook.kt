package com.yuk.miuihome.module

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.yuk.miuihome.view.utils.HomeContext
import com.yuk.miuihome.view.utils.LogUtil
import com.yuk.miuihome.view.utils.OwnSP
import com.yuk.miuihome.view.utils.ktx.*
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

class ModifyDockHook {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("dockSettings", false)) return
        try {
            val deviceConfigClass = "com.miui.home.launcher.DeviceConfig".findClass()
            val launcherClass = "com.miui.home.launcher.Launcher".findClass()
            val searchBarClass = "com.miui.home.launcher.SearchBarStyleData".findClass()
            val searchBarDesktopLayoutClass = "com.miui.home.launcher.SearchBarDesktopLayout$1".findClass()
            // Dock距屏幕两侧
            deviceConfigClass.hookBeforeMethod("calcSearchBarWidth", Context::class.java
            ) {
                val deviceWidth = px2dp(HomeContext.context.resources.displayMetrics.widthPixels)
                it.result = dp2px(deviceWidth - (OwnSP.ownSP.getFloat("dockSide", 3.0f) * 10))
            }
            // Dock距屏幕底部
            deviceConfigClass.hookBeforeMethod("calcSearchBarMarginBottom", Context::class.java, Boolean::class.java
            ) {
                it.result = dp2px((OwnSP.ownSP.getFloat("dockBottom", 2.3f) * 10))
            }
            // 图标距屏幕底部
            deviceConfigClass.hookBeforeMethod("calcHotSeatsMarginBottom", Context::class.java, Boolean::class.java, Boolean::class.java
            ) {
                it.result = dp2px((OwnSP.ownSP.getFloat("dockIconBottom", 3.5f) * 10))
            }
            // 页面指示器距离图标距离
            deviceConfigClass.hookBeforeMethod("calcHotSeatsMarginTop", Context::class.java, Boolean::class.java
            ) {
                it.result = dp2px((OwnSP.ownSP.getFloat("dockMarginTop", 0.6f) * 10))
            }
            // 页面指示器距离屏幕底部
            deviceConfigClass.hookBeforeMethod(
                "getWorkspaceIndicatorMarginBottom",
            ) {
                it.result = dp2px((OwnSP.ownSP.getFloat("dockMarginBottom", 11.0f) * 10))
            }
            // 宽度变化量
            deviceConfigClass.hookBeforeMethod("getSearchBarWidthDelta"
            ) {
                it.result = 0
            }

            launcherClass.hookAfterMethod("onCreate", Bundle::class.java
            ) { param ->

                searchBarDesktopLayoutClass.hookAfterMethod("doInBackground"
                ) {
                    if (it.thisObject.callMethod("doInBackground") != null) {
                        val rippleDrawable = it.thisObject.callMethod("doInBackground") as RippleDrawable
                        val gradientDrawable = rippleDrawable.getDrawable(0) as GradientDrawable
                        XposedBridge.log(gradientDrawable.toString())
                        gradientDrawable.cornerRadius = dp2px(OwnSP.ownSP.getFloat("dockRadius", 2.5f) * 10).toFloat()
                        gradientDrawable.setStroke(0, 0)
                        rippleDrawable.setDrawable(0, gradientDrawable)
                        it.result = rippleDrawable
                    }
                }
                val searchBarObject = param.thisObject.callMethod("getSearchBar") as FrameLayout
                val searchBarDesktop = searchBarObject.getChildAt(0) as RelativeLayout
                val searchBarDrawer = searchBarObject.getChildAt(1) as RelativeLayout
                val searchBarContainer = searchBarObject.parent as FrameLayout
                val searchEdgeLayout = searchBarContainer.parent as FrameLayout
                // 重新给搜索框容器排序
                searchEdgeLayout.removeView(searchBarContainer)
                searchEdgeLayout.addView(searchBarContainer, 0)
                // 清空搜索图标和小爱同学
                searchBarDesktop.removeAllViews()
                // 修改高度
                searchBarObject.layoutParams.height = dp2px((OwnSP.ownSP.getFloat("dockHeight", 7.9f) * 10))
                // 修改应用列表搜索框
                val mAllAppViewField = launcherClass.getDeclaredField("mAppsView")
                mAllAppViewField.isAccessible = true
                val mAllAppView = mAllAppViewField.get(param.thisObject) as RelativeLayout
                val mAllAppSearchView = mAllAppView.getChildAt(mAllAppView.childCount - 1) as FrameLayout
                searchBarObject.removeView(searchBarDrawer)
                mAllAppSearchView.addView(searchBarDrawer)
                searchBarDrawer.bringToFront()
                val layoutParams = searchBarDrawer.layoutParams as FrameLayout.LayoutParams
                searchBarDrawer.layoutParams.height = dp2px(45f)
                layoutParams.leftMargin = dp2px(15f)
                layoutParams.rightMargin = dp2px(15f)
                searchBarDrawer.layoutParams = layoutParams
            }
        } catch (e: XposedHelpers.ClassNotFoundError) {
            LogUtil.e(e)
        }
    }
}
