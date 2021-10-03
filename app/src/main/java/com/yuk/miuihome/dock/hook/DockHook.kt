package com.yuk.miuihome.dock.hook

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.dip2px
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.px2dip
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers

class DockHook {

    fun init() {
        if (OwnSP.ownSP.getBoolean(
                "dockSettings", false
            )
        ) {
            val deviceConfigClass = "com.miui.home.launcher.DeviceConfig".findClass()
            val launcherClass = "com.miui.home.launcher.Launcher".findClass()
            XposedHelpers.findAndHookMethod(
                deviceConfigClass,
                "calcHotSeatsMarginTop",
                Context::class.java,
                Boolean::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        param.args[1] = false
                        super.beforeHookedMethod(param)
                    }
                })
            // 图标区域底部边距
            XposedHelpers.findAndHookMethod(
                deviceConfigClass,
                "calcHotSeatsMarginBottom",
                Context::class.java,
                Boolean::class.java,
                Boolean::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        param.result =
                            dip2px((OwnSP.ownSP.getFloat("dockIconBottom", -1f) * 10).toInt())
                    }
                })
            // 搜索框宽度
            XposedHelpers.findAndHookMethod(
                deviceConfigClass,
                "calcSearchBarWidth",
                Context::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val context = param.args[0] as Context
                        val deviceWidth = px2dip(context.resources.displayMetrics.widthPixels)
                        param.result = dip2px(
                            deviceWidth - (OwnSP.ownSP.getFloat(
                                "dockSide",
                                -1f
                            ) * 10).toInt()
                        )
                    }
                })
            // Dock底部边距
            XposedHelpers.findAndHookMethod(
                deviceConfigClass,
                "calcSearchBarMarginBottom",
                Context::class.java,
                Boolean::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        param.result =
                            dip2px((OwnSP.ownSP.getFloat("dockBottom", -1f) * 10).toInt())
                    }
                })
            // 宽度变化量
            XposedHelpers.findAndHookMethod(
                deviceConfigClass,
                "getSearchBarWidthDelta",
                XC_MethodReplacement.returnConstant(0)
            )
            XposedHelpers.findAndHookMethod(
                launcherClass,
                "onCreate",
                Bundle::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)
                        val searchBarObject = XposedHelpers.callMethod(
                            param.thisObject,
                            "getSearchBar"
                        ) as FrameLayout
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
                            dip2px((OwnSP.ownSP.getFloat("dockHeight", -1f) * 10).toInt())
                        // 修改应用列表搜索框
                        val mAllAppViewField = launcherClass.getDeclaredField("mAppsView")
                        mAllAppViewField.isAccessible = true
                        val mAllAppView = mAllAppViewField.get(param.thisObject) as RelativeLayout
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
                })
        }
    }
}