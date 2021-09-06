package com.yuk.miuihome.activity

import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.*
import androidx.annotation.Keep
import com.yuk.miuihome.R
import com.yuk.miuihome.utils.dp2px
import miui.app.Activity

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(miui.R.style.Theme_DayNight)
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
                    dp2px(this@MainActivity, 15f),
                    dp2px(this@MainActivity, 28f),
                    dp2px(this@MainActivity, 15f)
                )
                addView(TextView(this@MainActivity).apply {
                    text = resources.getString(R.string.MainActivity1)
                })
                addView(TextView(this@MainActivity).apply {
                    text = resources.getString(R.string.MainActivity2)
                })
                addView(TextView(this@MainActivity).apply {
                    text = resources.getString(R.string.MainActivity3)
                })
                addView(TextView(this@MainActivity).apply {
                    text = ""
                })
                addView(TextView(this@MainActivity).apply {
                    text = ""
                })
                addView(Button(this@MainActivity).apply {
                    text = resources.getString(R.string.MainActivity4)
                    setOnClickListener { setContentView(getImageLayout()) }
                })
                addView(TextView(this@MainActivity).apply {
                    text = ""
                })
                addView(Button(this@MainActivity).apply {
                    text = resources.getString(R.string.MainActivity5)
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
                    addView(TextView(this@MainActivity).apply {
                        text = ""
                    })
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
                    addView(TextView(this@MainActivity).apply {
                        text = ""
                    })
                    addView(Button(this@MainActivity).apply {
                        text = resources.getString(R.string.ShowAppIcon)
                        setOnClickListener {
                            packageManager.setComponentEnabledSetting(
                                ComponentName(
                                    "com.yuk.miuihome",
                                    "com.yuk.miuihome.activity.EntryActivity"
                                ),
                                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                                PackageManager.DONT_KILL_APP
                            )
                            getSP().edit().putBoolean("shouldHide", false).apply()
                            finish()
                            val intent = Intent()
                            intent.component = ComponentName(
                                "com.yuk.miuihome",
                                "com.yuk.miuihome.activity.EntryActivity"
                            )
                            startActivity(intent)
                        }
                    })
                }
                addView(TextView(this@MainActivity).apply {
                    text = ""
                })
                addView(TextView(this@MainActivity).apply {
                    text = ""
                })
                if (moduleEnable()) {
                    addView(TextView(this@MainActivity).apply {
                        text = resources.getString(R.string.State) + " : " + resources.getString(R.string.ModuleEnable)
                    })
                } else {
                    addView(TextView(this@MainActivity).apply {
                        text = resources.getString(R.string.State) + " : " + resources.getString(R.string.ModuleNotEnable)
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
                    dp2px(this@MainActivity, 15f),
                    dp2px(this@MainActivity, 28f),
                    dp2px(this@MainActivity, 15f)
                )
                addView(Button(this@MainActivity).apply {
                    text = resources.getString(R.string.Back)
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

    @Keep
    private fun moduleEnable(): Boolean {
        return false
    }
}