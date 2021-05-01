package hk.qqlittleice.hook.miuihome

import android.app.AlertDialog
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import hk.qqlittleice.hook.miuihome.utils.dp2px
import hk.qqlittleice.hook.miuihome.utils.ktx.hookAfterMethod
import hk.qqlittleice.hook.miuihome.view.SettingSwitch
import hk.qqlittleice.hook.miuihome.view.SettingTextView

class MainHook {

    fun doHook() {
        "com.miui.home.settings.MiuiHomeSettingActivity".hookAfterMethod("onCreate", Bundle::class.java) {
            showSettingDialog()
        }
    }

    private fun showSettingDialog() {
        val dialogBuilder = AlertDialog.Builder(HomeContext.activity)
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 10f), dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 5f))
                addView(SettingTextView.FastBuilder(mText = "测试TextView").build())
                addView(SettingSwitch.FastBuilder(mText = "测试Switch", mKey = "test").build())
            })
        })
        dialogBuilder.setPositiveButton("关闭", null)
        dialogBuilder.show()
    }

}