package com.yuk.miuihome.view

import android.app.AlertDialog
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.isNightMode

class SettingBaseDialog {

    fun get(): AlertDialog.Builder {
        return if (isNightMode()) {
            AlertDialog.Builder(
                HomeContext.activity,
                android.R.style.Theme_Material_Dialog_Alert
            )
        } else {
            AlertDialog.Builder(
                HomeContext.activity,
                android.R.style.Theme_Material_Light_Dialog_Alert
            )
        }
    }
}