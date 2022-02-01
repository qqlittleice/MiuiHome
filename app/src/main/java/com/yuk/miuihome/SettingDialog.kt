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
}