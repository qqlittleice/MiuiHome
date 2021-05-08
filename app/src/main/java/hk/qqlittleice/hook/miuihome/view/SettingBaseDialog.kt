package hk.qqlittleice.hook.miuihome.view

import android.app.AlertDialog
import hk.qqlittleice.hook.miuihome.HomeContext
import hk.qqlittleice.hook.miuihome.utils.isNightMode

class SettingBaseDialog {

    fun get(): AlertDialog.Builder {
        return if (isNightMode(HomeContext.activity)) {
            AlertDialog.Builder(HomeContext.activity, android.R.style.Theme_DeviceDefault_Dialog_Alert) // miui.R.style.Theme_Dark_Dialog_Alert
        } else {
            AlertDialog.Builder(HomeContext.activity)
        }
    }

}
