package com.yuk.miuihome.view

import android.app.AlertDialog
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.isNightMode

class SettingBaseDialog {

    fun get(): AlertDialog.Builder {
        return if (isNightMode(HomeContext.activity)) {
            AlertDialog.Builder(HomeContext.activity, android.R.style.Theme_DeviceDefault_Dialog_Alert)
        } else {
            AlertDialog.Builder(HomeContext.activity)
        }
    }

}
