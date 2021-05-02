package hk.qqlittleice.hook.miuihome

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.XModuleResources
import androidx.annotation.Keep
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import hk.qqlittleice.hook.miuihome.utils.ktx.hookAfterAllMethods

@Keep
class XposedInit : IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit {

    companion object {
        const val TYPE_string = "string"
        const val TYPE_color = "color"
        const val TYPE_dimen = "dimen"
        const val TYPE_integer = "integer"
        const val TYPE_bool = "bool"
    }

    private lateinit var modulePath: String

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

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        if (resparam.packageName != Config.hookPackage) return
        val modRes = XModuleResources.createInstance(modulePath, resparam.res)
        resparam.res.apply {}
    }

}
