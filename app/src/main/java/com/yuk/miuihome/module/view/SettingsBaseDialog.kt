package com.yuk.miuihome.module.view

import android.app.AlertDialog
import com.yuk.miuihome.module.view.data.DataHelper
import com.yuk.miuihome.utils.ktx.isNightMode

class SettingsBaseDialog {

    fun get(): AlertDialog.Builder {
        return if (isNightMode(DataHelper.currentActivity)) {
            AlertDialog.Builder(DataHelper.currentActivity, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(DataHelper.currentActivity, android.R.style.Theme_Material_Light_Dialog_Alert)
        }
    }
}