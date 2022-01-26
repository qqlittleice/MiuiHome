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

    fun showDockDialog() {
        val dialogBuilder = SettingsBaseDialog().get()
        lateinit var dialog: AlertDialog
        dialogBuilder.apply {
            setView(ScrollView(currentActivity).apply {
                overScrollMode = 2
                addView(LinearLayout(currentActivity).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                    addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.DockSettings) + "」", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.DockFeature), mKey = "dockSettings") {
                        dialog.cancel()
                        showDockDialog()
                    }.build())
                    if (OwnSP.ownSP.getBoolean("dockSettings", false)) {
                        if (AndroidSDK == 30)
                            addView(SettingSwitch.FastBuilder(mText = moduleRes.getString(R.string.EnableDockBlur), mKey = "searchBarBlur").build())
                        if (!XposedInit.hasHookPackageResources)
                            addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.DockWarn), mColor = "#ff0c0c", mSize = SettingTextView.textSize).build())
                        else
                            addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockRoundedCorners), mKey = "dockRadius", minValue = 0, maxValue = 50, defValue = 25).build())
                        addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockHeight), mKey = "dockHeight", minValue = 50, maxValue = 150, defValue = 79).build())
                        addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockSide), mKey = "dockSide", minValue = 0, maxValue = 100, defValue = 30).build())
                        addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockBottom), mKey = "dockBottom", minValue = 0, maxValue = 150, defValue = 23).build())
                        addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockIconBottom), mKey = "dockIconBottom", minValue = 0, maxValue = 150, defValue = 35).build())
                        addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockMarginTop), mKey = "dockMarginTop", minValue = 0, maxValue = 100, defValue = 6).build())
                        addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.DockMarginBottom), mKey = "dockMarginBottom", minValue = 0, maxValue = 200, defValue = 110).build())
                    }
                })
            })
            if (OwnSP.ownSP.getBoolean("dockSettings", false)) {
                setPositiveButton(moduleRes.getString(R.string.Save), null)
                setNeutralButton(moduleRes.getString(R.string.Reset1)) { _, _ -> showModifyReset1() }
            }
        }
        dialog = dialogBuilder.show()
    }

    fun showModifyHorizontal() {
        val dialogBuilder = SettingsBaseDialog().get()
        dialogBuilder.apply {
            setView(ScrollView(currentActivity).apply {
                overScrollMode = 2
                addView(LinearLayout(currentActivity).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                    addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.HorizontalTaskViewOfAppCardSize) + "」", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.VerticalScreen), mKey = "task_horizontal1", minValue = 10, maxValue = 1500, divide = 1000, defValue = 1000).build())
                    addView(SettingSeekBar.FastBuilder(mText = moduleRes.getString(R.string.HorizontalScreen), mKey = "task_horizontal2", minValue = 10, maxValue = 1500, divide = 1000, defValue = 1000).build())
                })
            })
            setPositiveButton(moduleRes.getString(R.string.Save), null)
            setNeutralButton(moduleRes.getString(R.string.Reset2)) { _, _ -> showModifyReset2() }
        }
        dialogBuilder.show()
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

    private fun showModifyReset1() {
        val dialogBuilder = SettingsBaseDialog().get()
        dialogBuilder.apply {
            setView(ScrollView(currentActivity).apply {
                overScrollMode = 2
                addView(LinearLayout(currentActivity).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                    addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.Reset) + "」", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.Tips1)).build())
                })
            })
            setNeutralButton(moduleRes.getString(R.string.Yes)) { _, _ ->
                OwnSP.set("searchBarBlur", true)
                OwnSP.set("dockRadius", 2.5f)
                OwnSP.set("dockHeight", 7.9f)
                OwnSP.set("dockSide", 3f)
                OwnSP.set("dockBottom", 1.6f)
                OwnSP.set("dockIconBottom", 3.5f)
                OwnSP.set("dockMarginTop", 0.6f)
                OwnSP.set("dockMarginBottom", 11.0f)
                thread {
                    LogUtil.toast(moduleRes.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setPositiveButton(moduleRes.getString(R.string.Cancel), null)
        }
        dialogBuilder.show()
    }

    private fun showModifyReset2() {
        val dialogBuilder = SettingsBaseDialog().get()
        dialogBuilder.apply {
            setView(ScrollView(currentActivity).apply {
                overScrollMode = 2
                addView(LinearLayout(currentActivity).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dip2px(10), dip2px(6), dip2px(10), dip2px(6))
                    addView(SettingTextView.FastBuilder(mText = "「" + moduleRes.getString(R.string.Reset1) + "」", mColor = "#0C84FF", mSize = SettingTextView.text2Size).build())
                    addView(SettingTextView.FastBuilder(mText = moduleRes.getString(R.string.Tips3)).build())
                })
            })
            setNeutralButton(moduleRes.getString(R.string.Yes)) { _, _ ->
                OwnSP.set("task_horizontal1", 1.0f)
                OwnSP.set("task_horizontal2", 1.0f)
                thread {
                    LogUtil.toast(moduleRes.getString(R.string.Reboot2))
                    Thread.sleep(1000)
                    exitProcess(0)
                }
            }
            setPositiveButton(moduleRes.getString(R.string.Cancel), null)
        }
        dialogBuilder.show()
    }
}