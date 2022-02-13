package com.yuk.miuihome

import android.content.res.XModuleResources
import com.yuk.miuihome.view.utils.Config
import com.yuk.miuihome.view.utils.OwnSP
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam
import com.yuk.miuihome.view.utils.ktx.setTryReplacement
import kotlin.concurrent.thread

class ResHook(private val hookedRes: InitPackageResourcesParam) {

    private val modRes = XModuleResources.createInstance(XposedInit.modulePath, hookedRes.res)
    private fun getResId(type: String, name: String): Int = modRes.getIdentifier(name, type, Config.packageName)

    companion object { private var hasLoad = false }

    fun init() {
        thread {
            if (!hasLoad) Thread.sleep(400)
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
}
