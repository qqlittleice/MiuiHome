package hk.qqlittleice.hook.miuihome.utils

import android.content.res.Resources
import hk.qqlittleice.hook.miuihome.BuildConfig

data class ModuleRes(
    val resources: Resources,
    val classLoader: ClassLoader,
    val packageName: String = BuildConfig.APPLICATION_ID
)
