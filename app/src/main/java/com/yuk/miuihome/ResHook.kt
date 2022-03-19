package com.yuk.miuihome

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.XModuleResources
import android.content.res.XResources
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import com.github.kyuubiran.ezxhelper.init.InitFields.modulePath
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.Config.DrawableNameList
import com.yuk.miuihome.utils.Config.DrawableNameNewList
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.utils.ktx.hookLayout
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam
import kotlin.concurrent.thread

class ResHook(private val hookedRes: InitPackageResourcesParam) {

    companion object { private var hasLoad = false }
    fun init() {
        thread {
            if (!hasLoad) Thread.sleep(400)
            if (OwnSP.ownSP.getBoolean("dockSettings", false))
                hookedRes.res.hookLayout(Config.hostPackage, "layout", "layout_search_bar"
                ) {
                    val targetView = it.view
                    (if (XposedInit().checkIsAlpha() || XposedInit().checkVersionCode() >= 421153106L) DrawableNameNewList else DrawableNameList).forEach { drawableName -> resetDockRadius(targetView.context, drawableName) }
                }
        }
    }

    private fun resetDockRadius(context: Context, drawableName: String) {
        hookedRes.res.setReplacement(Config.hostPackage, "drawable", drawableName, object : XResources.DrawableLoader() {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun newDrawable(xres: XResources, id: Int): Drawable {
                val background = context.getDrawable(xres.getIdentifier(drawableName, "drawable", Config.hostPackage)) as RippleDrawable
                val backgroundShape = background.getDrawable(0) as GradientDrawable
                backgroundShape.cornerRadius = dp2px((OwnSP.ownSP.getFloat("dockRadius", 2.5f) * 10)).toFloat()
                backgroundShape.setStroke(0, 0)
                background.setDrawable(0, backgroundShape)
                return background
            }
        })
    }
}
