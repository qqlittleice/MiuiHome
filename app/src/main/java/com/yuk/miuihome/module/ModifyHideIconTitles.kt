package com.yuk.miuihome.module

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.hookAfterConstructor

class ModifyHideIconTitles {

    fun init() {
        if (OwnSP.ownSP.getBoolean("hideIconTitles", false)) {
            "com.miui.home.launcher.TitleTextView".hookAfterConstructor(Context::class.java, AttributeSet::class.java) {
                (it.thisObject as TextView).textSize = 0F
            }
        }
    }
}
