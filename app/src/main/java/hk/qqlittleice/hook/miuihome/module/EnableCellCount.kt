package hk.qqlittleice.hook.miuihome.module

import android.content.Context
import de.robv.android.xposed.XposedBridge
import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.ktx.setReturnConstant

class EnableCellCount {

    fun init() {

        if (OwnSP.ownSP.getBoolean("cellCount", false)) {
            try {
                "com.miui.home.launcher.DeviceConfig".setReturnConstant(
                    "getCellCountXMax",
                    Context::class.java,
                    result = 9
                )
                "com.miui.home.launcher.DeviceConfig".setReturnConstant(
                    "getCellCountYMax",
                    Context::class.java,
                    result = 9
                )
                "com.miui.home.launcher.DeviceConfig".setReturnConstant(
                    "getCellCountXMin",
                    Context::class.java,
                    result = 4
                )
                "com.miui.home.launcher.DeviceConfig".setReturnConstant(
                    "getCellCountYMin",
                    Context::class.java,
                    result = 5
                )
            }  catch (e: Throwable) {
                XposedBridge.log(e.message)
            }
        }
    }
}