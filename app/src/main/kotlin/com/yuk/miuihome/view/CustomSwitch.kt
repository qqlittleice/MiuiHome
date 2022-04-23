package com.yuk.miuihome.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Switch
import com.yuk.miuihome.utils.ktx.dp2px
import java.lang.reflect.Field

class CustomSwitch(context: Context) : Switch(context) {

    @SuppressLint("DiscouragedPrivateApi")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val switchWidth: Field = Switch::class.java.getDeclaredField("mSwitchWidth")
        switchWidth.isAccessible = true
        switchWidth.setInt(this, dp2px(48f))
    }
}