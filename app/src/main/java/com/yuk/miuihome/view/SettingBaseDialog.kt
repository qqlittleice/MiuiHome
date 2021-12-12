package com.yuk.miuihome.view

import android.app.AlertDialog
import com.yuk.miuihome.utils.HomeContext
import com.yuk.miuihome.utils.ktx.isNightMode

class SettingBaseDialog {

    fun get(): AlertDialog.Builder {
        return if (isNightMode(HomeContext.activity)) {
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