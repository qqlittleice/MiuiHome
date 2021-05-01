package hk.qqlittleice.hook.miuihome

import android.app.AlertDialog
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import hk.qqlittleice.hook.miuihome.module.ModifyBlurLevel
import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.dp2px
import hk.qqlittleice.hook.miuihome.utils.ktx.hookAfterMethod
import hk.qqlittleice.hook.miuihome.view.SettingTextView

class MainHook {

    private val sharedPreferences = OwnSP.ownSP
    private val editor by lazy { sharedPreferences.edit() }

    fun doHook() {
        "com.miui.home.settings.MiuiHomeSettingActivity".hookAfterMethod("onCreate", Bundle::class.java) {
            showSettingDialog()
        }
        ModifyBlurLevel().init()
    }

    private fun showSettingDialog() {
        val dialogBuilder = AlertDialog.Builder(HomeContext.activity)
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 10f), dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 5f))
                addView(SettingTextView.FastBuilder(mText = "MiuiHome设置", mSize = SettingTextView.titleSize).build())
                addView(SettingTextView.FastBuilder(mText = "后台模糊级别", mSize = SettingTextView.title2Size) { showModifyBlurLevel() }.build())
            })
        })
        dialogBuilder.setPositiveButton("关闭", null)
        dialogBuilder.show()
    }


    private fun showModifyBlurLevel() {
        val dialogBuilder = AlertDialog.Builder(HomeContext.activity)
        val mKey = "blurLevel"
        lateinit var dialog: AlertDialog
        fun saveValue(value: String) {
            editor.putString(mKey, value)
            editor.apply()
        }
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                addView(SettingTextView.FastBuilder(mText = "完整模糊", mSize = SettingTextView.title2Size) {
                    saveValue("COMPLETE")
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = "简单模糊", mSize = SettingTextView.title2Size) {
                    saveValue("SIMPLE")
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = "无模糊", mSize = SettingTextView.title2Size) {
                    saveValue("NONE")
                    dialog.dismiss()
                }.build())
            })
        })
        dialog = dialogBuilder.show()
    }

}
