package com.yuk.miuihome.view

import android.app.AlertDialog
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.isNightMode

class SettingBaseDialog {

    fun get(): AlertDialog.Builder {
        return if (isNightMode(HomeContext.activity)) {
            AlertDialog.Builder(HomeContext.activity, miui.R.style.Theme_Dark_Dialog)
        } else {
            AlertDialog.Builder(HomeContext.activity, miui.R.style.Theme_Light_Dialog)
        }
    }
}