package com.yuk.miuihome.module

import android.graphics.Color
import android.widget.FrameLayout
import android.widget.ImageView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.getObjectField
import com.zhenxiang.blur.WindowBlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius

class EnableFolderIconBlur {

    fun init() {
        if (!OwnSP.ownSP.getBoolean("folderBlur", false) || Config.AndroidSDK < 31) return
        val value = OwnSP.ownSP.getFloat("folderBlurCorner", 40f)
        findMethod("com.miui.home.launcher.FolderIcon".findClass(), true)
        { name == "onFinishInflate" }.hookAfter {
            val mIconImageView = it.thisObject.getObjectField("mIconImageView") as ImageView
            val mIconContainer = mIconImageView.parent as FrameLayout
            val child0 = mIconContainer.getChildAt(0) as ImageView //LauncherIconImageView -> 文件夹的背景
            val child2 = mIconContainer.getChildAt(2) as ImageView //LauncherIconImageView -> 不知道是啥
            val blur = WindowBlurFrameLayout(mIconContainer.context)
            blur.blurController.apply {
                backgroundColour = Color.parseColor("#44FFFFFF")
                cornerRadius = CornersRadius.all(value)
            }
            mIconContainer.removeView(child0)
            mIconContainer.removeView(child2)
            val lp = child2.layoutParams as FrameLayout.LayoutParams
            lp.setMargins(dp2px(-1.5f), dp2px(-1.5f), dp2px(-1.5f), dp2px(-1.5f))
            blur.addView(child2)
            mIconContainer.addView(blur, 0)
        }
    }
}