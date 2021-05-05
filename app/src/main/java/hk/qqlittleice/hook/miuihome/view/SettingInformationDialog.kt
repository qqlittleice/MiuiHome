package hk.qqlittleice.hook.miuihome.view

import android.app.AlertDialog
import android.widget.LinearLayout
import android.widget.ScrollView
import hk.qqlittleice.hook.miuihome.HomeContext
import hk.qqlittleice.hook.miuihome.utils.LogUtil
import hk.qqlittleice.hook.miuihome.utils.OwnSP
import hk.qqlittleice.hook.miuihome.utils.dp2px
import kotlin.concurrent.thread

class SettingInformationDialog(private val message: String, private val seconds: Int, private val key: String) {

    fun build(): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(HomeContext.activity, android.R.style.Theme_DeviceDefault_Dialog_Alert)
        var timeLeft = seconds
        dialogBuilder.setView(ScrollView(HomeContext.activity).apply {
            overScrollMode = 2
            addView(LinearLayout(HomeContext.activity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 10f), dp2px(HomeContext.context, 20f), dp2px(HomeContext.context, 5f))
                addView(SettingTextView.FastBuilder(mText = message).build())
            })
        })
        dialogBuilder.apply {
            setTitle("重要信息")
            setPositiveButton("我已阅读", null)
            setNegativeButton("取消") { _, _ ->
                OwnSP.ownSP.edit().apply {
                    putBoolean(key, false)
                    apply()
                }
                LogUtil.toast("已取消")
            }
            setCancelable(false)
        }
        dialogBuilder.show().apply {
            getButton(AlertDialog.BUTTON_POSITIVE).apply {
                isClickable = false
                thread {
                    do {
                        this.post { this.text = "我已阅读(${timeLeft}s)" }
                        Thread.sleep(1000)
                    } while (--timeLeft > 0)
                    this.post {
                        this.text = "我已阅读"
                        this.isClickable = true
                    }
                }
            }
            return this
        }

    }

}