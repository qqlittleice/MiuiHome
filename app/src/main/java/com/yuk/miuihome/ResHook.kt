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
import com.yuk.miuihome.utils.ktx.dip2px
import com.yuk.miuihome.utils.ktx.hookLayout
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam
import com.yuk.miuihome.utils.ktx.setTryReplacement
import kotlin.concurrent.thread

class ResHook(private val hookedRes: InitPackageResourcesParam) {

    private val modRes = XModuleResources.createInstance(XposedInit.modulePath, hookedRes.res)
    private fun getResId(type: String, name: String): Int = modRes.getIdentifier(name, type, Config.packageName)

    companion object {
        private var hasLoad = false
    }

    fun init() {
        thread {
            if (!hasLoad) Thread.sleep(400)
            if (OwnSP.ownSP.getBoolean("dockSettings", false))
                hookedRes.res.hookLayout(Config.hookPackage, "layout", "layout_search_bar"
                ) {
                    val targetView = it.view
                    (if (XposedInit().checkAlpha() || XposedInit().checkVersionCode() >= 421153106L)
                        DrawableNameNewList else DrawableNameList).forEach {
                            drawableName -> resetDockRadius(targetView.context, drawableName)
                        }
                }
            val backgroundTextSize = OwnSP.ownSP.getInt("backgroundTextSize", 13)
            val message: String = OwnSP.ownSP.getString("recentText", "").toString()
            if (!(backgroundTextSize == -1 || backgroundTextSize == 13))
                hookedRes.res.setTryReplacement(Config.hookPackage, "dimen", "recents_task_view_header_title_text_size", modRes.fwd(getResId("dimen", "sp$backgroundTextSize")))
            if (OwnSP.ownSP.getBoolean("buttonPadding", false))
                hookedRes.res.setTryReplacement(Config.hookPackage, "dimen", "recents_task_view_header_button_padding", modRes.fwd(getResId("dimen", "sp100")))
            if (OwnSP.ownSP.getBoolean("smallWindow", false))
                hookedRes.res.setTryReplacement(Config.hookPackage, "dimen", "recent_tv_small_window_margin_start", modRes.fwd(getResId("dimen", "dp_200")))
            if (OwnSP.ownSP.getBoolean("cleanUp", false)) {
                hookedRes.res.setTryReplacement(Config.hookPackage, "drawable", "btn_clear_all", modRes.fwd(R.drawable.a))
                hookedRes.res.setTryReplacement(Config.hookPackage, "drawable", "notifications_clear_all", modRes.fwd(R.drawable.a))
            }
            if (OwnSP.ownSP.getString("recentText", "") != "")
                hookedRes.res.setTryReplacement(Config.hookPackage, "string", "recents_empty_message", message)
        }
    }

    private fun resetDockRadius(context: Context, drawableName: String) {
        hookedRes.res.setTryReplacement(Config.hookPackage, "drawable", drawableName, object : XResources.DrawableLoader() {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun newDrawable(xres: XResources, id: Int): Drawable {
                val background = context.getDrawable(xres.getIdentifier(drawableName, "drawable", Config.hookPackage)) as RippleDrawable
                val backgroundShape = background.getDrawable(0) as GradientDrawable
                backgroundShape.cornerRadius = dip2px((OwnSP.ownSP.getFloat("dockRadius", 2.5f) * 10).toInt()).toFloat()
                backgroundShape.setStroke(0, 0)
                background.setDrawable(0, backgroundShape)
                return background
            }
        })
    }
}
