package com.yuk.miuihome.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.yuk.miuihome.R
import kotlin.concurrent.thread

class EntryActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(android.R.style.Theme_DeviceDefault_Settings)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entry_activity)
        thread {
            Thread.sleep(1000)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}