package hk.qqlittleice.hook.miuihome

import android.app.AlertDialog
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import hk.qqlittleice.hook.miuihome.module.*
import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.dp2px
import hk.qqlittleice.hook.miuihome.utils.ktx.hookAfterMethod
import hk.qqlittleice.hook.miuihome.view.SettingSwitch
import hk.qqlittleice.hook.miuihome.view.SettingTextView

class MainHook {

    private val sharedPreferences = OwnSP.ownSP
    private val editor by lazy { sharedPreferences.edit() }

    fun doHook() {
        "com.miui.home.settings.MiuiHomeSettingActivity".hookAfterMethod("onCreate", Bundle::class.java) {
            showSettingDialog()
        }
        ModifyBlurLevel().init()
        EnableSmoothAnimation().init()
        EnableBlurWhenOpenFolder().init()
        EnableMamlDownload().init()
        EnableClockGadget().init()
        EnableSimpleAnimation().init()
    }

    private fun showSettingDialog() {
        val dialogBuilder = AlertDialog.Builder(HomeContext.activity)
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 10f), dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 5f))
                addView(SettingTextView.FastBuilder(mText = "MiuiHome设置", mSize = SettingTextView.titleSize).build())
                addView(SettingTextView.FastBuilder(mText = "模糊设置", mSize = SettingTextView.text2Size).build())
                addView(SettingTextView.FastBuilder(mText = "后台模糊级别", mSize = SettingTextView.textSize) { showModifyBlurLevel() }.build())
                addView(SettingTextView.FastBuilder(mText = "其他设置", mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = "平滑动画", mKey = "smoothAnimation").build())
                addView(SettingSwitch.FastBuilder(mText = "文件夹模糊", mKey = "blurWhenOpenFolder").build())
                addView(SettingSwitch.FastBuilder(mText = "水波纹下载特效", mKey = "mamlDownload").build())
                addView(SettingTextView.FastBuilder(mText = "扩展设置", mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = "时钟常显", mKey = "clockGadget").build())
                addView(SettingSwitch.FastBuilder(mText = "简单动画", mKey = "simpleAnimation").build())
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
                addView(SettingTextView.FastBuilder(mText = "完整模糊", mSize = SettingTextView.textSize) {
                    saveValue("COMPLETE")
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = "简单模糊", mSize = SettingTextView.textSize) {
                    saveValue("SIMPLE")
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = "无模糊", mSize = SettingTextView.textSize) {
                    saveValue("NONE")
                    dialog.dismiss()
                }.build())
            })
        })
        dialog = dialogBuilder.show()
    }

}
