package com.yuk.miuihome.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.yuk.miuihome.R
import kotlin.concurrent.thread

class EntryActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            setTheme(miui.R.style.Theme_DayNight_NoTitle)
        } else {
            setTheme(android.R.style.Theme_DeviceDefault_DayNight)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entry_activity)
        thread {
            Thread.sleep(1000)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}