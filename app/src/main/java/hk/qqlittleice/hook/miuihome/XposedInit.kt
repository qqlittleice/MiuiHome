package hk.qqlittleice.hook.miuihome

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.annotation.Keep
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import hk.qqlittleice.hook.miuihome.utils.ktx.hookAfterAllMethods

@Keep
class XposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != Config.hookPackage) return
        EzXHelperInit.initHandleLoadPackage(lpparam)
        XposedHelpers.findAndHookMethod("com.miui.home.launcher.Application", lpparam.classLoader, "attachBaseContext", Context::class.java, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                HomeContext.application = param.thisObject as Application
                HomeContext.context = param.args[0] as Context
                HomeContext.classLoader = HomeContext.context.classLoader
                EzXHelperInit.initAppContext(HomeContext.application)
                XposedHelpers.findClass("android.app.Instrumentation", HomeContext.classLoader)
                    .hookAfterAllMethods("newActivity") { activityParam ->
                        HomeContext.activity = activityParam.result as Activity
                    }
                ResHook().init()
                MainHook().doHook()
            }
        })
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
    }

    companion object {
        lateinit var modulePath: String
    }

}
