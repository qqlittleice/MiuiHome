package com.yuk.miuihome.view

import android.app.AlertDialog
import android.view.KeyEvent
import com.yuk.miuihome.HomeContext
import com.yuk.miuihome.utils.isNightMode

class SettingBaseDialog {

    fun get(dismissByBack: Boolean = true): AlertDialog.Builder {
        return if (isNightMode(HomeContext.activity)) {
            AlertDialog.Builder(
                HomeContext.activity,
                android.R.style.Theme_Material_Dialog_Alert
            ).also {
                if (dismissByBack)
                    it.setOnKeyListener { dialog, keyCode, _ ->
                        if (keyCode == KeyEvent.KEYCODE_BACK)
                            dialog.dismiss()
                        true
                    }
            }
        } else {
            AlertDialog.Builder(
                HomeContext.activity,
                android.R.style.Theme_Material_Light_Dialog_Alert
            ).also {
                if (dismissByBack)
                    it.setOnKeyListener { dialog, keyCode, _ ->
                        if (keyCode == KeyEvent.KEYCODE_BACK)
                            dialog.dismiss()
                        true
                    }
            }
        }
    }
}