package com.yuk.miuihome.module

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.github.kyuubiran.ezxhelper.init.InitFields.appContext
import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.getMethodByClassOrObject
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*
import de.robv.android.xposed.XposedHelpers

class ModifyDockHook {

    var blur: FrameLayout? = null
    var init = false

    fun init() {
        if (!OwnSP.ownSP.getBoolean("dockSettings", false)) return
        try {
            val deviceConfigClass = "com.miui.home.launcher.DeviceConfig".findClass()
            val launcherClass = "com.miui.home.launcher.Launcher".findClass()
            // Dock距屏幕两侧
            deviceConfigClass.hookBeforeMethod("calcSearchBarWidth", Context::class.java
            ) {
                val deviceWidth = px2dp(appContext.resources.displayMetrics.widthPixels)
                it.result = dp2px(deviceWidth - OwnSP.ownSP.getFloat("dockSide", 3.0f) * 10)
            }
            // Dock距屏幕底部
            deviceConfigClass.hookBeforeMethod("calcSearchBarMarginBottom", Context::class.java, Boolean::class.java
            ) {
                it.result = dp2px(OwnSP.ownSP.getFloat("dockBottom", 2.3f) * 10)
            }
            // 图标距屏幕底部
            deviceConfigClass.hookBeforeMethod("calcHotSeatsMarginBottom", Context::class.java, Boolean::class.java, Boolean::class.java
            ) {
                it.result = dp2px(OwnSP.ownSP.getFloat("dockIconBottom", 3.5f) * 10)
            }
            // 页面指示器距离图标距离
            deviceConfigClass.hookBeforeMethod("calcHotSeatsMarginTop", Context::class.java, Boolean::class.java
            ) {
                it.result = dp2px(OwnSP.ownSP.getFloat("dockMarginTop", 0.6f) * 10)
            }
            // 页面指示器距离屏幕底部
            deviceConfigClass.hookBeforeMethod("getWorkspaceIndicatorMarginBottom",
            ) {
                it.result = dp2px(OwnSP.ownSP.getFloat("dockMarginBottom", 11.0f) * 10)
            }
            // 宽度变化量
            deviceConfigClass.hookBeforeMethod("getSearchBarWidthDelta"
            ) {
                it.result = 0
            }
            "com.miui.home.launcher.SearchBarDesktopLayout\$1".hookAfterMethod("doInBackground"
            ) {
                val searchBarDesktop = it.result
                val background = when (searchBarDesktop) {
                    is RippleDrawable -> searchBarDesktop.getDrawable(0) as GradientDrawable
                    is GradientDrawable -> searchBarDesktop
                    else -> return@hookAfterMethod
                }
                background.cornerRadius = dp2px((OwnSP.ownSP.getFloat("dockRadius", 2.5f) * 10)).toFloat()
                if (!init && OwnSP.ownSP.getBoolean("searchBarBlur", false) && Config.AndroidSDK == 31) {
                    blur?.let { it1 -> setViewBlurForS(it1) }
                }
                background.setStroke(0, 0)
                when (searchBarDesktop) {
                    is RippleDrawable -> searchBarDesktop.setDrawable(0, background)
                }
            }
            launcherClass.hookAfterMethod("onCreate", Bundle::class.java
            ) {
                val searchBarObject = it.thisObject.callMethod("getSearchBar") as FrameLayout
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
                // 添加模糊
                if (OwnSP.ownSP.getBoolean("searchBarBlur", false) && Config.AndroidSDK == 31) {
                    searchBarObject.removeAllViews()
                    blur = FrameLayout(searchBarObject.context)
                    blur?.addView(searchBarDesktop)
                    searchBarObject.addView(blur)
                }
                // 修改应用列表搜索框
                val mAllAppViewField = launcherClass.getDeclaredField("mAppsView")
                mAllAppViewField.isAccessible = true
                val mAllAppView = mAllAppViewField.get(it.thisObject) as RelativeLayout
                val mAllAppSearchView = mAllAppView.getChildAt(mAllAppView.childCount - 1) as FrameLayout
                searchBarObject.removeView(searchBarDrawer)
                mAllAppSearchView.addView(searchBarDrawer)
                searchBarDrawer.bringToFront()
                val layoutParams = searchBarDrawer.layoutParams as FrameLayout.LayoutParams
                searchBarDrawer.layoutParams.height = dp2px(43f)
                layoutParams.leftMargin = dp2px(15f)
                layoutParams.rightMargin = dp2px(15f)
                searchBarDrawer.layoutParams = layoutParams
            }
        } catch (e: XposedHelpers.ClassNotFoundError) {
            Log.ex(e)
        }
    }

    private fun setViewBlurForS(view: View) {
        val viewRootImplMethod = view.getMethodByClassOrObject("getViewRootImpl")
        val viewRootImpl = viewRootImplMethod.invoke(view)
        if (viewRootImpl != null) init = true
        try {
            val drawable = viewRootImpl?.callMethod("createBackgroundBlurDrawable")
            drawable?.callMethod("setBlurRadius", 100)
            drawable?.callMethod("setCornerRadius", dp2px(OwnSP.ownSP.getFloat("dockRadius", 2.5f) * 10).toFloat())
            drawable?.callMethod("setColor", Color.parseColor("#33FFFFFF"))
            view.background = drawable as? Drawable
        } catch (e: Throwable) {
        }
    }
}