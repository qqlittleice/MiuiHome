package com.yuk.miuihome.utils

import android.content.res.Resources
import com.yuk.miuihome.BuildConfig

data class ModuleRes(
    val resources: Resources,
    val classLoader: ClassLoader,
    val packageName: String = BuildConfig.APPLICATION_ID
)
