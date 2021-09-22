package com.yuk.miuihome.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.yuk.miuihome.R
import kotlin.concurrent.thread

class EntryActivity : Activity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(android.R.style.ThemeOverlay_DeviceDefault_Accent_DayNight)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entry_activity)
        thread {
            Thread.sleep(1200)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}