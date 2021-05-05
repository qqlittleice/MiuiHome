package hk.qqlittleice.hook.miuihome

import android.content.res.Resources
import com.github.kyuubiran.ezxhelper.utils.invokeMethod
import hk.qqlittleice.hook.miuihome.utils.LogUtil

class ResHook {

    fun init(res: Resources = HomeContext.application.resources, modulePath: String = XposedInit.modulePath) {

        try {
            res.getString(R.string.res_hooked)
            return
        } catch (ignore: Resources.NotFoundException) {}
        try {
            val assets = res.assets
            assets.invokeMethod("addAssetPath", arrayOf(modulePath), arrayOf(String::class.java))
            LogUtil.e(" 尝试注入！modulePath = $modulePath")
            try {
                LogUtil.e("Resources injection result: ${res.getString(R.string.res_hooked)}")
            } catch (e: Resources.NotFoundException) {
                LogUtil.e("Resources injection failed!")
            }
        } catch (e: Exception) {
            LogUtil.e(e)
        }
    }

}
