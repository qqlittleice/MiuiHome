package hk.qqlittleice.hook.miuihome

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
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
        ModifyAnimDurationRatio().init()
        TestHook().init()
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
                addView(SettingTextView.FastBuilder(mText = "后台模糊级别") { showModifyBlurLevel() }.build())
                addView(SettingTextView.FastBuilder(mText = "其他设置", mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = "平滑动画", mKey = "smoothAnimation").build())
                addView(SettingSwitch.FastBuilder(mText = "文件夹模糊", mKey = "blurWhenOpenFolder").build())
                addView(SettingSwitch.FastBuilder(mText = "水波纹下载特效", mKey = "mamlDownload").build())
                addView(SettingTextView.FastBuilder(mText = "扩展设置", mSize = SettingTextView.text2Size).build())
                addView(SettingSwitch.FastBuilder(mText = "时钟常显", mKey = "clockGadget").build())
                addView(SettingSwitch.FastBuilder(mText = "简单动画", mKey = "simpleAnimation").build())
                addView(SettingTextView.FastBuilder(mText = "动画速度调节") { showModifyAnimationLevel() }.build())
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

    private fun showModifyAnimationLevel() {
        val dialogBuilder = AlertDialog.Builder(HomeContext.activity)
        val mKey = "animationLevel"
        var nowValue: Float = sharedPreferences.getFloat("animationLevel", 0f)
        lateinit var valueTextView: TextView
        fun saveValue(value: Float) {
            nowValue = value
            editor.putFloat(mKey, value)
            editor.apply()
        }
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                addView(SettingTextView.FastBuilder(mText = "动画速度调节", mSize = SettingTextView.text2Size).build())
                addView(SeekBar(HomeContext.context).apply {
                    min = 10
                    max = 500
                    progress = (nowValue * 100).toInt()
                    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            saveValue(progress.toFloat() / 100)
                            valueTextView.text = "${nowValue}f"
                        }
                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                    })
                })
                addView(LinearLayout(HomeContext.context).apply {
                    addView(TextView(HomeContext.context).apply {
                        text = "0.1f"
                        layoutParams = LinearLayout.LayoutParams(70, LinearLayout.LayoutParams.MATCH_PARENT)
                    })
                    addView(TextView(HomeContext.context).apply {
                        text = "${nowValue}f"
                        weightSum = 1f
                        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        valueTextView = this
                        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                    })
                    addView(TextView(HomeContext.context).apply {
                        text = "5.0f"
                        textAlignment = TextView.TEXT_ALIGNMENT_TEXT_END
                        layoutParams = LinearLayout.LayoutParams(70, LinearLayout.LayoutParams.MATCH_PARENT)
                    })
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    gravity = Gravity.CENTER_VERTICAL
                    (this.layoutParams as LinearLayout.LayoutParams).apply {
                        topMargin = dp2px(HomeContext.context, 5f)
                        setPaddingRelative(dp2px(HomeContext.context, 12f), paddingTop, paddingEnd, dp2px(HomeContext.context, 5f))
                    }
                })
            })
        })
        dialogBuilder.show()
    }

}