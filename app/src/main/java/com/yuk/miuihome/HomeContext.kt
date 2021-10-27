package com.yuk.miuihome

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build

@SuppressLint("StaticFieldLeak")
object HomeContext {
    lateinit var application: Application
    lateinit var context: Context
    lateinit var classLoader: ClassLoader
    lateinit var activity: Activity
    lateinit var resInstance: ResInject
    var isAlpha = false
    var isWidgetLauncher = false
    var versionCode: Long = -1L
    val AndroidSDK: Int = Build.VERSION.SDK_INT

    val drawableNameList = arrayOf(
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
    val drawableNameNewList = arrayOf(
        "bg_search_bar_black8_white11",
        "bg_search_bar_button_dark",
        "bg_search_bar_button_light",
        "bg_search_bar_dark",
        "bg_search_bar_light",
        "bg_search_bar_input_dark",
        "bg_search_bar_input_light"
    )
}