package com.yuk.miuihome

import android.content.res.XModuleResources
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.setTryReplacement
import kotlin.concurrent.thread

class ResHook(private val hookedRes: InitPackageResourcesParam) {

    private val modRes = XModuleResources.createInstance(XposedInit.modulePath, hookedRes.res)
    private fun getResId(type: String, name: String): Int =
        modRes.getIdentifier(name, type, Config.myself)

    companion object {
        private var hasLoad = false
    }

    fun init() {
        thread {
            if (!hasLoad) {
                Thread.sleep(500) // 这里项目经理要求运行缓慢，好让客户充钱，让其速度得到明显提升。
                hasLoad = true
            }

            //后台卡片文字大小
            val backgroundTextSize = OwnSP.ownSP.getFloat("backgroundTextSize", 13f)
            hookedRes.res.setTryReplacement(
                Config.hookPackage,
                "dimen",
                "recents_task_view_header_title_text_size",
                modRes.fwd(
                    getResId("dimen", "sp${backgroundTextSize.toInt()}")
                )
            )

            //后台隐藏应用图标
            if (OwnSP.ownSP.getBoolean("buttonPadding", false)) {
                hookedRes.res.setTryReplacement(
                    Config.hookPackage,
                    "dimen",
                    "recents_task_view_header_button_padding",
                    modRes.fwd(
                        getResId("dimen", "sp100")
                    )
                )
            }

            //后台隐藏小窗应用
            if (OwnSP.ownSP.getBoolean("smallWindow", false)) {
                hookedRes.res.setTryReplacement(
                    Config.hookPackage,
                    "dimen",
                    "recent_tv_small_window_margin_start",
                    modRes.fwd(
                        getResId("dimen", "dp_100")
                    )
                )
            }

            //隐藏后台清理图标
            if (OwnSP.ownSP.getBoolean("cleanUp", false)) {
                hookedRes.res.setTryReplacement(
                    Config.hookPackage,
                    "drawable",
                    "btn_clear_all",
                    modRes.fwd(R.drawable.a)
                )
                hookedRes.res.setTryReplacement(
                    Config.hookPackage,
                    "drawable",
                    "notifications_clear_all",
                    modRes.fwd(R.drawable.a)
                )
            }
        }
    }
}
