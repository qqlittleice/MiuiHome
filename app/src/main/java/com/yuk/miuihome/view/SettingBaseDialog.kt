package com.yuk.miuihome.view

import android.app.AlertDialog
import android.os.Build
import com.yuk.miuihome.HomeContext

class SettingBaseDialog {

    fun get(): AlertDialog.Builder {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            AlertDialog.Builder(
                HomeContext.activity,
                miui.R.style.Theme_DayNight_Dialog
            )
        } else {
            AlertDialog.Builder(
                HomeContext.activity,
                android.R.style.Theme_DeviceDefault_Dialog
            )
        }
    }
}