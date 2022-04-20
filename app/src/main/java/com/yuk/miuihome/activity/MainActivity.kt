package com.yuk.miuihome.activity

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Switch
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.data.DefValue
import cn.fkj233.ui.activity.view.SpinnerV
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import cn.fkj233.ui.dialog.MIUIDialog
import cn.fkj233.ui.dialog.NewDialog
import com.github.kyuubiran.ezxhelper.init.InitFields
import com.github.kyuubiran.ezxhelper.utils.showToast
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.ktx.isDarkMode


class MainActivity : MIUIActivity() {

    private val activity = this

    init {
        initView {
            registerMain("[WIP] ${getString(R.string.app_name)}", false) {
                // TODO
                // Demo
                TextSummaryArrow(TextSummaryV("showTest", onClickListener = {
                    showFragment("test")
                }))
                Text("showDialog0", onClickListener = {
                    NewDialog(activity) {
                        setTitle("Test")
                        setMessage("TestMessage")
                        Button("0") {
                            dismiss()
                        }
                        Button("1") {
                            dismiss()
                        }
                        Button("2", cancelStyle = true) {
                            dismiss()
                        }
                    }.show()
                })
                Text("showDialog1", onClickListener = {
                    MIUIDialog(activity) {
                        setTitle("Test")
                        setMessage("TestMessage")
                        setEditText("", "test")
                        setLButton("Cancel") {
                            dismiss()
                        }
                        setRButton("OK") {
                            dismiss()
                        }
                    }.show()
                })
                TextSummaryArrow(TextSummaryV("test", tips = "summary", onClickListener = {}))
                TextWithSwitch(TextV("test"), SwitchV("test0"))
                TextSummaryWithSwitch(TextSummaryV("test", tips = "summary"), SwitchV("test1"))
                TextWithSpinner(TextV("Spinner"), SpinnerV("test0") {
                    add("test0") { showToast("select test0") }
                    add("test1") { showToast("select test1") }
                    add("test2") { showToast("select test2") }
                    add("test3") { showToast("select test3") }
                })
                TextSummaryWithSpinner(TextSummaryV("Spinner", tips = "Summary"), SpinnerV("test0") {
                    add("test0") { showToast("select test0") }
                    add("test1") { showToast("select test1") }
                    add("test2") { showToast("select test2") }
                    add("test3") { showToast("select test3") }
                })
                Line()
                TitleText("Title")
                TextSummaryArrow(TextSummaryV("test", tips = "summary", onClickListener = {}))
                Text("SeekbarWithText")
                SeekBarWithText("seekbar", 0, 100, 0)
                Line()
                TitleText("DataBinding")
                val binding = GetDataBinding(object : DefValue {
                    override fun getValue(): Any {
                        return safeSP.getBoolean("binding", false)
                    }
                }) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextWithSwitch(TextV("data-binding"), SwitchV("binding", dataBindingSend = binding.bindingSend))
                TextWithSwitch(TextV("test"), SwitchV("test2", dataBindingRecv = binding.binding.getRecv(1)))
                TextSummaryArrow(TextSummaryV("test", onClickListener = {}), dataBindingRecv = binding.binding.getRecv(2))
            }

            registerMenu("Menu") {
                Text("ThisMenu")
                Text("ThisMenu")
            }

            register("test", "test", false) {
                Text("ThisTest")
                Text("ThisTest1")
                TextSummaryArrow(TextSummaryV("showTest2", onClickListener = {
                    showFragment("test2")
                }))
            }

            register("test2", "test2", true) {
                Text("ThisTest2")
                Text("ThisTest3")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setSP(getPreferences(0))
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(if (isDarkMode()) "#FF000000" else "#FFFFFFFF")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun isDarkMode(): Boolean = activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

}