package com.yuk.miuihome

import android.app.AlertDialog
import android.view.View
import android.widget.*
import com.yuk.miuihome.XposedInit.Companion.moduleRes
import com.yuk.miuihome.module.ModifyBlurLevel
import com.yuk.miuihome.module.view.SettingsBaseDialog
import com.yuk.miuihome.module.view.data.DataHelper.currentActivity
import com.yuk.miuihome.utils.Config.AndroidSDK
import com.yuk.miuihome.utils.LogUtil
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.dip2px
import com.yuk.miuihome.view.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class SettingDialog {

    private fun customHookDialog() {
        val argsEditText = arrayListOf<EditText>()
        val argsLinearLayout = LinearLayout(currentActivity).also { it.orientation = LinearLayout.VERTICAL }

        fun createArgsEditText(): View {
            val linearView = LinearLayout(currentActivity).also { layout ->
                lateinit var editText: EditText
                layout.orientation = LinearLayout.HORIZONTAL
                layout.addView(EditText(currentActivity).apply {
                    argsEditText.add(this)
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    editText = this
                })
                layout.addView(Button(currentActivity).apply {
                    text = "X"
                    setOnClickListener {
                        argsEditText.remove(editText)
                        argsLinearLayout.removeView(layout)
                    }
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5f)
                })
            }
            return linearView
        }

        val dialogBuilder = SettingsBaseDialog().get()
        dialogBuilder.apply {
            setTitle("自定义Hook")
            setView(ScrollView(currentActivity).apply {
                overScrollMode = 2
                addView(LinearLayout(currentActivity).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                    addView(SettingTextView.FastBuilder(mText = "Class:").build())
                    val classEditText = EditText(currentActivity)
                    addView(classEditText)
                    addView(SettingTextView.FastBuilder(mText = "Method name:").build())
                    val methodEditText = EditText(currentActivity)
                    addView(methodEditText)
                    addView(SettingTextView.FastBuilder(mText = "arg(s):").build())
                    addView(argsLinearLayout)
                    addView(Button(currentActivity).apply {
                        text = "X"
                        setOnClickListener { argsLinearLayout.addView(createArgsEditText()) }
                    })
                    addView(SettingTextView.FastBuilder(mText = "result(null直接不输入即可):").build())
                    val resultEditText = EditText(currentActivity)
                    addView(resultEditText)
                    addView(SettingTextView.FastBuilder(mText = "result type(null直接不输入即可):").build())
                    val resultTypeEditText = EditText(currentActivity)
                    addView(resultTypeEditText)
                })
            })
        }.show()
    }

    fun showModifyBlurLevel() {
        val dialogBuilder = SettingsBaseDialog().get()
        lateinit var dialog: AlertDialog
        lateinit var onClick: View
        dialogBuilder.setView(ScrollView(currentActivity).apply {
            overScrollMode = 2
            addView(LinearLayout(currentActivity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.TaskViewBlurLevel) + "」", mSize = SettingTextView.text2Size, mColor = "#0C84FF").build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.CompleteBlur)) {
                    OwnSP.set("blurLevel", 2f)
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.TestBlur)) {
                    OwnSP.set("blurLevel", 3f)
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.BasicBlur), show = ModifyBlurLevel.checked) {
                    OwnSP.set("blurLevel", 4f)
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.SimpleBlur)) {
                    OwnSP.set("blurLevel", 1f)
                    onClick = it
                    dialog.dismiss()
                }.build())
                addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.NoneBlur)) {
                    OwnSP.set("blurLevel", 0f)
                    onClick = it
                    dialog.dismiss()
                }.build())
            })
        })
        dialog = dialogBuilder.show().apply {
            setOnDismissListener {
                try {
                    LogUtil.toast(moduleRes.getString(R.string.TaskViewBlurSetTo) + " : ${(onClick as TextView).text}")
                } catch (ignore: Exception) {
                }
            }
        }
    }
}