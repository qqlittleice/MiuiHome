package com.yuk.miuihome.module

import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.getObjectField
import com.zhenxiang.blur.WindowBlurFrameLayout
import com.zhenxiang.blur.model.CornersRadius

class EnableFolderIconBlur {
    fun init() {
        if (!OwnSP.ownSP.getBoolean("folderBlur", false) || Build.VERSION.SDK_INT < 31) return
        val value = OwnSP.ownSP.getFloat("folderBlurCorner", 30f)
        val value1 = OwnSP.ownSP.getFloat("folderBlurSide", 1.2f) * 100
        findMethod("com.miui.home.launcher.FolderIcon".findClass(), true)
        { name == "onFinishInflate" }.hookAfter { hookParam ->
            val mIconImageView = hookParam.thisObject.getObjectField("mIconImageView") as ImageView
            val mIconContainer = mIconImageView.parent as FrameLayout
            val blur = WindowBlurFrameLayout(mIconContainer.context)
            val view = FrameLayout(mIconImageView.context)
            blur.blurController.apply {
                backgroundColour = Color.parseColor("#44FFFFFF")
                cornerRadius = CornersRadius.all(value)
            }
            mIconContainer.removeView(mIconImageView)
            blur.addView(view)
            mIconContainer.addView(blur, 0)
            val lp1 = blur.layoutParams as FrameLayout.LayoutParams
            lp1.gravity = Gravity.CENTER
            lp1.height = value1.toInt()
            lp1.width = value1.toInt()
        }
    }
}