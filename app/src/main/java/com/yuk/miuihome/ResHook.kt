package com.yuk.miuihome

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.XModuleResources
import android.content.res.XResources
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.Config.DrawableNameList
import com.yuk.miuihome.utils.Config.DrawableNameNewList
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.dp2px
import com.yuk.miuihome.utils.ktx.hookLayout
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam
import com.yuk.miuihome.utils.ktx.setTryReplacement
import kotlin.concurrent.thread

class ResHook(private val hookedRes: InitPackageResourcesParam) {

    private val modRes = XModuleResources.createInstance(XposedInit.modulePath, hookedRes.res)
    private fun getResId(type: String, name: String): Int = modRes.getIdentifier(name, type, Config.packageName)

    companion object { private var hasLoad = false }

    fun init() {
        thread {
            if (!hasLoad) Thread.sleep(400)
            if (OwnSP.ownSP.getBoolean("dockSettings", false))
                hookedRes.res.hookLayout(Config.hookPackage, "layout", "layout_search_bar"
                ) {
                    val targetView = it.view
                    (if (XposedInit().checkAlpha() || XposedInit().checkVersionCode() >= 421153106L) DrawableNameNewList else DrawableNameList).forEach { drawableName -> resetDockRadius(targetView.context, drawableName) }
                }
            val message: String = OwnSP.ownSP.getString("recentText", "").toString()
            if (OwnSP.ownSP.getString("recentText", "") != "")
                hookedRes.res.setTryReplacement(Config.hookPackage, "string", "recents_empty_message", message)
        }
    }

    private fun resetDockRadius(context: Context, drawableName: String) {
        hookedRes.res.setTryReplacement(Config.hookPackage, "drawable", drawableName, object : XResources.DrawableLoader() {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun newDrawable(xres: XResources, id: Int): Drawable {
                val drawable = context.getDrawable(xres.getIdentifier(drawableName, "drawable", Config.hookPackage))
                val background = when (drawable) {
                    is RippleDrawable -> drawable.getDrawable(0) as GradientDrawable
                    else -> drawable as GradientDrawable
                }
                background.cornerRadius = dp2px((OwnSP.ownSP.getFloat("dockRadius", 2.5f) * 10)).toFloat()
                background.setStroke(0, 0)
                when (drawable) {
                    is RippleDrawable -> drawable.setDrawable(0, background)
                }
                return background
            }
        })
    }
}
