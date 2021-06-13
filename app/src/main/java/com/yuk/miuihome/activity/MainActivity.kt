package com.yuk.miuihome.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.*
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.dp2px
import miui.app.Activity

class MainActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(miui.R.style.Theme_Light_Settings)
        super.onCreate(savedInstanceState)
        setContentView(getMainLayout())
    }

    @SuppressLint("SetTextI18n")
    private fun getMainLayout(): ScrollView {
        val scrollView = ScrollView(this)
        scrollView.apply {
            overScrollMode = 2
            addView(LinearLayout(this@MainActivity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(
                    dp2px(this@MainActivity, 28f),
                    dp2px(this@MainActivity, 20f),
                    dp2px(this@MainActivity, 20f),
                    dp2px(this@MainActivity, 20f))
                addView(TextView(this@MainActivity).apply {
                    text = "请先在Lsposed管理器中激活模块，并重新启动系统桌面。"
                })
                addView(TextView(this@MainActivity).apply {
                    text = "模块的设置界面在MIUI系统桌面的设置中！"
                })
                addView(TextView(this@MainActivity).apply {
                    text = "如果不清楚在哪，请点击\"让我康康！\"查看图片。"
                })
                addView(TextView(this@MainActivity).apply {
                    text = ""
                })
                addView(TextView(this@MainActivity).apply {
                    text = ""
                })
                addView(Button(this@MainActivity).apply {
                    text = "让我康康！"
                    setOnClickListener { setContentView(getImageLayout()) }
                })
                addView(TextView(this@MainActivity).apply {
                    text = ""
                })
                addView(Button(this@MainActivity).apply {
                    text = "打开MIUI桌面设置"
                    setOnClickListener {
                        val intent = Intent()
                        intent.component = ComponentName("com.miui.home", "com.miui.home.settings.MiuiHomeSettingActivity")
                        startActivity(intent)
                    }
                })
                if (! getSP().getBoolean("shouldHide", false)) {
                    addView(TextView(this@MainActivity).apply {
                        text = ""
                    })
                    addView(Button(this@MainActivity).apply {
                        text = "隐藏桌面图标"
                        setOnClickListener {
                            packageManager.setComponentEnabledSetting(mComponentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
                            getSP().edit().putBoolean("shouldHide", true).apply()
                        }
                    })
                    addView(TextView(this@MainActivity).apply {
                        text = ""
                    })
                    addView(TextView(this@MainActivity).apply {
                        text = "注意：一旦选择隐藏图标，只能通过卸载来恢复。"
                    })
                }
            })
        }
        return scrollView
    }

    @SuppressLint("ResourceType")
    private fun getImageLayout(): ScrollView {
        val scrollView = ScrollView(this)
        scrollView.apply {
            overScrollMode = 2
            addView(LinearLayout(this@MainActivity).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(
                    dp2px(this@MainActivity, 28f),
                    dp2px(this@MainActivity, 20f),
                    dp2px(this@MainActivity, 20f),
                    dp2px(this@MainActivity, 5f))

                addView(Button(this@MainActivity).apply {
                    text = "返回"
                    setOnClickListener { setContentView(getMainLayout()) }
                })
                addView(ImageView(this@MainActivity).apply {
                    setImageResource(R.raw.image)
                })
            })
        }
        return scrollView
    }

    private fun getSP(): SharedPreferences {
        return getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

}