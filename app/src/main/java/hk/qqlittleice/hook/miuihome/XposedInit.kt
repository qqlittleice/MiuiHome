package hk.qqlittleice.hook.miuihome

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.annotation.Keep
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import hk.qqlittleice.hook.miuihome.utils.ktx.hookAfterAllMethods

@Keep
class XposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != Config.hookPackage) return
        XposedHelpers.findAndHookMethod("com.miui.home.launcher.Application", lpparam.classLoader, "attachBaseContext", Context::class.java, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                HomeContext.application = param.thisObject as Application
                HomeContext.context = param.args[0] as Context
                HomeContext.classLoader = HomeContext.context.classLoader
                HomeContext.myRes = ResInject().init()
                MainHook().doHook()

            }
        })
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam?) {
        hasHookPackageResources = true
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
    }

    companion object {
        lateinit var modulePath: String
        var hasHookPackageResources = false
    }

}
