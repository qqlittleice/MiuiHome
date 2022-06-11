package com.yuk.miuihome.module

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.*
import com.zhenxiang.blur.WindowBlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius

class EnableFolderIconBlur {

    @SuppressLint("DiscouragedApi")
    fun init() {
        if (!OwnSP.ownSP.getBoolean("folderBlur", false) || Build.VERSION.SDK_INT < 31) return
        "com.miui.home.launcher.Launcher".hookAfterMethod("onCreate", Bundle::class.java
        ) { param ->
            val value = OwnSP.ownSP.getFloat("folderBlurCorner", 32f)
            val value1 = OwnSP.ownSP.getFloat("folderBlurMargins", -2.1f)
            findMethod("com.miui.home.launcher.FolderIcon".findClass(), true)
            { name == "onFinishInflate" }.hookAfter {
                val mIconImageView = it.thisObject.getObjectField("mIconImageView") as ImageView
                val mIconContainer = mIconImageView.parent as FrameLayout
                val child0 = mIconContainer.getChildAt(0) as ImageView //LauncherIconImageView -> 文件夹的背景
                val blur = WindowBlurFrameLayout(mIconContainer.context)
                blur.blurController.apply {
                    backgroundColour = Color.parseColor("#44FFFFFF")
                    cornerRadius = CornersRadius.all(value)
                }
                val lp = child0.layoutParams as FrameLayout.LayoutParams
                lp.setMargins(dp2px(value1),dp2px(value1),dp2px(value1),dp2px(value1))
                mIconContainer.removeView(child0)
                blur.addView(child0)
                mIconContainer.addView(blur, 0)
            }
        }
    }
}