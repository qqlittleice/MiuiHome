package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.utils.ktx.callMethod
import hk.qqlittleice.hook.miuihome.utils.ktx.findClass
import hk.qqlittleice.hook.miuihome.utils.ktx.getObjectField
import hk.qqlittleice.hook.miuihome.utils.ktx.hookBeforeMethod

class TestHook {

    fun init() {
        val mSurfaceControl = "com.android.systemui.shared.recents.system.SurfaceControlCompat".findClass()
        "com.android.systemui.shared.recents.system.TransactionCompat".hookBeforeMethod("show", mSurfaceControl) {
            it.thisObject.getObjectField("mTransaction")?.callMethod("hide", it.args[0].getObjectField("mSurfaceControl"))
            it.result = it.thisObject
        }
    }

}
