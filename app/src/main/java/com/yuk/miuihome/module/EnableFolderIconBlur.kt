package com.yuk.miuihome.module

import android.graphics.Color
import android.widget.FrameLayout
import android.widget.ImageView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.getObjectField
import com.yuk.miuihome.view.BlurFrameLayout

class EnableFolderIconBlur {

    fun init() {
        findMethod("com.miui.home.launcher.FolderIcon".findClass(), true)
        { name == "onFinishInflate" }.hookAfter {
            val mIconImageView = it.thisObject.getObjectField("mIconImageView") as ImageView
            val mIconContainer = mIconImageView.parent as FrameLayout
            val child0 = mIconContainer.getChildAt(0) as ImageView //LauncherIconImageView -> 文件夹的背景
            val blur = BlurFrameLayout(mIconContainer.context)
            blur.setBlurRadius(100)
            blur.setCornerRadius(50f)
            blur.setColor(Color.parseColor("#33626262"))
            mIconContainer.removeView(child0)
            blur.addView(child0)
            mIconContainer.addView(blur, 0)
        }
    }
}