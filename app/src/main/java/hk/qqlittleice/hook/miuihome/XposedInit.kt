package hk.qqlittleice.hook.miuihome

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.annotation.Keep
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import hk.qqlittleice.hook.miuihome.utils.ktx.hookAfterAllMethods

@Keep
class XposedInit : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != Config.hookPackage) return
        XposedHelpers.findAndHookMethod(Application::class.java, "attach", Context::class.java, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                HomeContext.context = param.args[0] as Context
                HomeContext.classLoader = HomeContext.context.classLoader
                XposedHelpers.findClass("android.app.Instrumentation", HomeContext.classLoader)
                        .hookAfterAllMethods("newActivity") { activityParam ->
                            HomeContext.activity = activityParam.result as Activity
                        }
                MainHook().doHook()
            }
        })
    }

}
