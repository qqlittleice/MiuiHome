package com.yuk.miuihome.activity

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import com.yuk.miuihome.R
import miui.app.Activity
import kotlin.concurrent.thread

lateinit var mComponentName: ComponentName

class EntryActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(miui.R.style.Theme_DayNight_NoTitle)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entry_activity)
        mComponentName = componentName
        thread {
            Thread.sleep(1000)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
