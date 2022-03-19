package com.yuk.miuihome.utils

import android.os.Build
import com.yuk.miuihome.BuildConfig

object Config {
    const val modulePackage = BuildConfig.APPLICATION_ID
    const val hostPackage = "com.miui.home"
    const val hostActivityProxy = "com.miui.home.settings.DefaultHomeSettings"
    const val SP_NAME = "MiuiHomePerf"
    const val TAG = "MiuiHome"
    val AndroidSDK: Int = Build.VERSION.SDK_INT
    val DrawableNameList = arrayOf(
        "bg_search_bar_white85_black5",
        "bg_search_bar_black20_white10",
        "bg_search_bar_black8_white11",
        "bg_search_bar_d9_15_non",
        "bg_search_bar_e3_25_non",
        "bg_search_bar_button_dark",
        "bg_search_bar_button_light",
        "bg_search_bar_dark",
        "bg_search_bar_light"
    )
    val DrawableNameNewList = arrayOf(
        "bg_search_bar_black8_white11",
        "bg_search_bar_button_dark",
        "bg_search_bar_button_light",
        "bg_search_bar_dark",
        "bg_search_bar_light",
        "bg_search_bar_input_dark",
        "bg_search_bar_input_light"
    )
}
