package hk.qqlittleice.hook.miuihome

import android.content.res.XModuleResources
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam
import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.ktx.setReturnConstant
import hk.qqlittleice.hook.miuihome.utils.ktx.setTryReplacement
import kotlin.concurrent.thread

class ResHook(private val hookedRes: InitPackageResourcesParam) {

    private val modRes = XModuleResources.createInstance(XposedInit.modulePath, hookedRes.res)
    private fun getResId(type: String, name: String): Int = modRes.getIdentifier(name, type, Config.myself)

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
                modRes.fwd(getResId("dimen", "sp${backgroundTextSize.toInt()}"))
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

            //解锁桌面布局
            if (OwnSP.ownSP.getBoolean("cellCount", false)) {
                hookedRes.res.setTryReplacement(
                    Config.hookPackage,
                    "integer",
                    "config_cell_count_x_max",
                    9)
                hookedRes.res.setTryReplacement(
                    Config.hookPackage,
                    "integer",
                    "config_cell_count_y_max",
                    9)
                hookedRes.res.setTryReplacement(
                    Config.hookPackage,
                    "integer",
                    "config_cell_count_x_min",
                    4)
                hookedRes.res.setTryReplacement(
                    Config.hookPackage,
                    "integer",
                    "config_cell_count_y_min",
                    4)
            } else {
               return@thread
            }
        }
    }
}
