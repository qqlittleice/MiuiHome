package com.yuk.miuihome.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.dp2px
import android.widget.TextView

class MainActivity : Activity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(android.R.style.Theme_DeviceDefault_DayNight)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
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
                    dp2px(this@MainActivity, 15f),
                    dp2px(this@MainActivity, 15f),
                    dp2px(this@MainActivity, 15f),
                    dp2px(this@MainActivity, 15f)
                )
                addView(TextView(this@MainActivity).apply {
                    text = resources.getString(R.string.MainActivity1) + "\n"
                })
                addView(Button(this@MainActivity).apply {
                    text = resources.getString(R.string.OpenMiuiHomeSettings)
                    setOnClickListener {
                        val intent = Intent()
                        intent.component = ComponentName(
                            "com.miui.home",
                            "com.miui.home.settings.MiuiHomeSettingActivity"
                        )
                        startActivity(intent)
                    }
                })
                if (!getSP().getBoolean("shouldHide", false)) {
                    addView(Button(this@MainActivity).apply {
                        text = resources.getString(R.string.HideAppIcon)
                        setOnClickListener {
                            try {
                                packageManager.setComponentEnabledSetting(
                                    ComponentName(
                                        "com.yuk.miuihome",
                                        "com.yuk.miuihome.activity.EntryActivity"
                                    ),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP
                                )
                            } catch (e: ActivityNotFoundException) {
                                e.printStackTrace()
                            }
                            getSP().edit().putBoolean("shouldHide", true).apply()
                        }
                    })
                } else {
                    addView(Button(this@MainActivity).apply {
                        text = resources.getString(R.string.ShowAppIcon)
                        setOnClickListener {
                            packageManager.setComponentEnabledSetting(
                                ComponentName(
                                    packageName,
                                    "com.yuk.miuihome.activity.EntryActivity"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                                PackageManager.DONT_KILL_APP
                            )
                            getSP().edit().putBoolean("shouldHide", false).apply()
                            finish()
                            val intent = Intent()
                            intent.component = ComponentName(
                                packageName,
                                "com.yuk.miuihome.activity.EntryActivity"
                            )
                            startActivity(intent)
                        }
                    })
                }
                addView(Button(this@MainActivity).apply {
                    text = resources.getString(R.string.Github)
                    setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        intent.data = Uri.parse("https://github.com/1767523953/MiuiHome")
                        startActivity(intent)
                    }
                })
                addView(Button(this@MainActivity).apply {
                    text = resources.getString(R.string.Coolapk)
                    setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        intent.data = Uri.parse("coolmarket://www.coolapk.com/apk/com.yuk.miuihome")
                        startActivity(intent)
                    }
                })
                addView(Button(this@MainActivity).apply {
                    text = resources.getString(R.string.Telegram)
                    setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.addCategory(Intent.CATEGORY_BROWSABLE)
                        intent.data = Uri.parse("https://t.me/MiuiHome_Xposed")
                        startActivity(intent)
                    }
                })
                addView(TextView(this@MainActivity).apply {
                    text = "\n" + resources.getString(R.string.State) + ":"

                })
                addView(TextView(this@MainActivity).apply {
                    text =
                        if (moduleEnable())
                            resources.getString(R.string.ModuleEnable)
                        else
                            resources.getString(R.string.ModuleNotEnable)
                    if (moduleEnable()) this.setTextColor(Color.GREEN) else this.setTextColor(Color.RED)
                })
                addView(TextView(this@MainActivity).apply {
                    text = "\n" + resources.getString(R.string.MainActivity2)
                })
            })
        }
        return scrollView
    }

    private fun getSP(): SharedPreferences {
        return getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    @Keep
    private fun moduleEnable(): Boolean {
        return false
    }
}