package hk.qqlittleice.hook.miuihome

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import dalvik.system.DexClassLoader
import hk.qqlittleice.hook.miuihome.utils.LogUtil
import hk.qqlittleice.hook.miuihome.utils.ModuleRes

class ResHook {

    lateinit var moduleRes: ModuleRes

    companion object {
        lateinit var res: Resources
    }

    fun init(modulePath: String = XposedInit.modulePath): ModuleRes {
        try {
            val assetManager = AssetManager::class.java.newInstance()
            val method = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
            method.invoke(assetManager, modulePath)
            val resources = Resources(
                assetManager,
                HomeContext.context.resources.displayMetrics,
                HomeContext.context.resources.configuration
            )
            val file = HomeContext.application.getDir("dex", Context.MODE_PRIVATE)
            if (!file.exists()) file.mkdir()
            val classLoader = DexClassLoader(modulePath, file.absolutePath, null, HomeContext.classLoader)
            moduleRes = ModuleRes(resources, classLoader)
            LogUtil.e("成功加载资源")
            res = resources
            return moduleRes
        } catch (e: Throwable) {
            LogUtil.e(e)
            throw e
        }
    }

    fun getResourceID(type: String, fieldName: String): Int {
        val resId: Int
        val name = "hk.qqlittleice.hook.miuihome.R$$type"
        try {
            val cls = moduleRes.classLoader.loadClass(name)
            resId = cls.getField(fieldName).get(null) as Int
            return resId
        } catch (e: Throwable) {
            LogUtil.e(e)
            throw e
        }
    }

}
