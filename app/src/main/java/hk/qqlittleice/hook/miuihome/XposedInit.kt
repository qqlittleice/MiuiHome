package hk.qqlittleice.hook.miuihome

import android.app.Application
import android.content.Context
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedInit : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam?.packageName != Config.hookPackage) return
        XposedHelpers.findAndHookMethod(Application::class.java, "attach", Context::class.java, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                HomeContext.context = param.args[0] as Context
                HomeContext.classLoader = HomeContext.context.classLoader
                MainHook().doHook()
            }
        })
    }

}
