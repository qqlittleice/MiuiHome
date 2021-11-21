package com.yuk.miuihome.module

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.yuk.miuihome.Config.AndroidSDK
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.dip2px
import com.yuk.miuihome.utils.ktx.*
import com.yuk.miuihome.utils.px2dip
import de.robv.android.xposed.XposedHelpers.callMethod
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class ModifyDockHook {

    fun init() {
        if (ownSP.getBoolean("dockSettings", false)) {
            if (AndroidSDK >= 30) {
                try {
                    readStream(
                        Runtime.getRuntime()
                            .exec("su -c settings put system key_home_screen_search_bar_show_initiate 1").inputStream
                    )
                } catch (ignore: Exception) {
                }
            }
            val deviceConfigClass = "com.miui.home.launcher.DeviceConfig".findClass()
            val launcherClass = "com.miui.home.launcher.Launcher".findClass()
            // 页面指示器距离图标距离
            deviceConfigClass.replaceMethod(
                "calcHotSeatsMarginTop",
                Context::class.java,
                Boolean::class.java
            ) {
                dip2px((ownSP.getFloat("dockMarginTop", 0.6f) * 10).toInt())
            }
            // 页面指示器距离屏幕底部
            deviceConfigClass.replaceMethod(
                "getWorkspaceIndicatorMarginBottom",
            ) {
                dip2px((ownSP.getFloat("dockMarginBottom", 11.0f) * 10).toInt())
            }
            // 图标距屏幕底部
            deviceConfigClass.replaceMethod(
                "calcHotSeatsMarginBottom",
                Context::class.java,
                Boolean::class.java,
                Boolean::class.java
            ) {
                dip2px((ownSP.getFloat("dockIconBottom", 3.5f) * 10).toInt())
            }
            // Dock距屏幕两侧
            deviceConfigClass.replaceMethod(
                "calcSearchBarWidth",
                Context::class.java
            ) {
                val deviceWidth = px2dip(HomeContext.context.resources.displayMetrics.widthPixels)
                dip2px(deviceWidth - (ownSP.getFloat("dockSide", 3.0f) * 10).toInt())
            }
            // Dock距屏幕底部
            deviceConfigClass.replaceMethod(
                "calcSearchBarMarginBottom",
                Context::class.java,
                Boolean::class.java
            ) {
                dip2px((ownSP.getFloat("dockBottom", 2.3f) * 10).toInt())
            }
            // 宽度变化量
            deviceConfigClass.replaceMethod("getSearchBarWidthDelta") { 0 }
            launcherClass.hookAfterMethod(
                "onCreate",
                Bundle::class.java
            ) {
                val searchBarObject = callMethod(it.thisObject, "getSearchBar") as FrameLayout
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
                searchBarObject.layoutParams.height =
                    dip2px((ownSP.getFloat("dockHeight", 7.9f) * 10).toInt())
                // 修改应用列表搜索框
                val mAllAppViewField = launcherClass.getDeclaredField("mAppsView")
                mAllAppViewField.isAccessible = true
                val mAllAppView = mAllAppViewField.get(it.thisObject) as RelativeLayout
                val mAllAppSearchView =
                    mAllAppView.getChildAt(mAllAppView.childCount - 1) as FrameLayout
                searchBarObject.removeView(searchBarDrawer)
                mAllAppSearchView.addView(searchBarDrawer)
                searchBarDrawer.bringToFront()
                val layoutParams = searchBarDrawer.layoutParams as FrameLayout.LayoutParams
                searchBarDrawer.layoutParams.height = dip2px(45)
                layoutParams.leftMargin = dip2px(15)
                layoutParams.rightMargin = dip2px(15)
                searchBarDrawer.layoutParams = layoutParams
            }
        }
    }

    private fun readStream(input: InputStream) {
        val reader = BufferedReader(InputStreamReader(input))
        var read = ""
        while (true) {
            val temp: String = reader.readLine() ?: break
            read += temp
        }
    }
}