package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.BuildConfig
import hk.qqlittleice.hook.miuihome.HomeContext
import hk.qqlittleice.hook.miuihome.utils.LogUtil
import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.ktx.callMethod
import hk.qqlittleice.hook.miuihome.utils.ktx.findClass
import hk.qqlittleice.hook.miuihome.utils.ktx.getObjectField
import hk.qqlittleice.hook.miuihome.utils.ktx.hookBeforeMethod
import java.io.BufferedReader
import java.io.InputStreamReader

class TestHook {

    fun init() {

        if (! OwnSP.ownSP.getBoolean("TESTONLY", false)) return
        val exec = Runtime.getRuntime().exec("pm path " + BuildConfig.APPLICATION_ID)
        val bufferedReader = BufferedReader(InputStreamReader(exec.inputStream))
        var string = ""
        while (true) {
            val readLine = bufferedReader.readLine() ?: break
            string += readLine
        }
        val packagePath = string.replace("package:", "")
        LogUtil.e(packagePath)
    }

}
