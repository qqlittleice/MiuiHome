package com.yuk.miuihome.module

import android.os.Build
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.ViewSwitcher
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.yuk.miuihome.XposedInit
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.getObjectField
import com.zhenxiang.blur.WindowBlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius

class EnableAllAppsContainerViewBlur {
    fun init() {
        if (!OwnSP.ownSP.getBoolean("allAppsBlur", false) || Build.VERSION.SDK_INT < 31) return
        findMethod("com.miui.home.launcher.allapps.BaseAllAppsContainerView".findClass(), true)
        { name == "onFinishInflate" }.hookAfter { hookParam ->
            val mCategoryContainer = hookParam.thisObject.getObjectField("mCategoryContainer") as ViewSwitcher
            val appsView = mCategoryContainer.parent as RelativeLayout
            val blur = WindowBlurFrameLayout(mCategoryContainer.context)
            val radius = XposedInit().getCornerRadiusTop().toFloat()
            blur.blurController.apply {
                cornerRadius = CornersRadius.custom(radius, radius, 0f, 0f)
            }
            val view = View(mCategoryContainer.context)
            blur.addView(view)
            (view.layoutParams as FrameLayout.LayoutParams).apply {
                width = FrameLayout.LayoutParams.MATCH_PARENT
                height = FrameLayout.LayoutParams.MATCH_PARENT
            }
            appsView.addView(blur, 0)
            findMethod("com.miui.home.launcher.allapps.BaseAllAppsContainerView".findClass(), true)
            { name == "onResume" }.hookAfter {
                blur.refreshDrawableState()
            }
        }

    }
}
