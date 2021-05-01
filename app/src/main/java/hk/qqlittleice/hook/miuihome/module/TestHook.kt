package hk.qqlittleice.hook.miuihome.module

import hk.qqlittleice.hook.miuihome.utils.ktx.*

class TestHook {

    fun init() {
        val surfaceControlCompat = "com.android.systemui.shared.recents.system.SurfaceControlCompat".findClass()
        "com.miui.home.recents.DimLayer".hookBeforeMethod("dim", Float::class.java, surfaceControlCompat, Boolean::class.java) {
            it.thisObject.setFloatField("mCurrentAlpha", 0.0f)
        }
    }

}